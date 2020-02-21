package ru.dimasokol.quotty.presentation

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.dimasokol.quotty.R
import ru.dimasokol.quotty.data.QuoteResponse
import ru.dimasokol.quotty.data.QuotesRepository
import ru.dimasokol.quotty.exceptions.BusinessException
import ru.dimasokol.quotty.utils.StringsLoader
import kotlin.coroutines.CoroutineContext

class MainScreenPresenter(
    private val stringsLoader: StringsLoader,
    private val repository: QuotesRepository,
    private val mainDispatcher: CoroutineContext,
    private val ioDispatcher: CoroutineContext
) {
    private var currentJob: Job? = null

    private var state: MainScreenState = MainScreenState(
        true,
        stringsLoader.getString(R.string.please_wait),
        stringsLoader.getString(R.string.app_authors)
    )

    private var view: MainScreenView? = null

    private var lastQuoteId: String? = null

    private var lastError: Throwable? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        state = makeErrorState()
        lastError = throwable
        notifyView()
    }

    private var presenterScope = CoroutineScope(exceptionHandler)

    fun attachView(view: MainScreenView?) {
        this.view = view
        notifyView()
    }

    fun detachView() {
        view = null
    }

    fun loadNextQuote() {
        if (currentJob?.isActive == true) {
            return
        }

        if (currentJob?.isCancelled == true) {
            // Я не придумал лучшего, чем пересоздавать остановленный скоуп
            presenterScope = CoroutineScope(exceptionHandler)
        }

        state = makeLoadingState()
        notifyView()

        currentJob = presenterScope.launch(mainDispatcher) {
            val result = doLoadNextQuote()
            state = MainScreenState(
                false,
                result.text,
                if (result.author.length > 0) result.author else stringsLoader.getString(R.string.unknown_author)
            )

            lastQuoteId = result.url

            notifyView()
        }
    }

    private suspend fun doLoadNextQuote(): QuoteResponse = withContext(ioDispatcher) {
        repository.loadNextQuote(lastQuoteId)
    }

    private fun makeLoadingState(): MainScreenState =
        MainScreenState(true, state.text, state.author)

    private fun makeErrorState(): MainScreenState = MainScreenState(false, state.text, state.author)

    private fun notifyView() {
        view?.renderState(state)

        if (lastError != null && view != null) {

            if (lastError is BusinessException) {
                view?.displayError(stringsLoader.getString((lastError as BusinessException).messageRes));
            } else {
                view?.displayError(stringsLoader.getString(R.string.error_generic))
            }

            lastError = null
        }
    }
}
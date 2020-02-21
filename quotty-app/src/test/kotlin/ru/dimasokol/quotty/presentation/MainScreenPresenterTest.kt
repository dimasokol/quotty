package ru.dimasokol.quotty.presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import ru.dimasokol.quotty.R
import ru.dimasokol.quotty.data.QuoteResponse
import ru.dimasokol.quotty.data.QuotesRepository
import ru.dimasokol.quotty.exceptions.NetworkException
import ru.dimasokol.quotty.utils.StringsLoader

class MainScreenPresenterTest {

    lateinit var view: MainScreenView
    lateinit var presenter: MainScreenPresenter
    lateinit var stringsLoader: StringsLoader
    lateinit var repository: QuotesRepository

    @Before
    fun setUp() {
        view = mockk(relaxed = true)
        stringsLoader = mockk()
        repository = mockk()

        every { stringsLoader.getString(R.string.please_wait) } returns WAIT
        every { stringsLoader.getString(R.string.app_authors) } returns APP_AUTHORS
        every { stringsLoader.getString(R.string.error_network) } returns NETWORK_ERROR
        every { stringsLoader.getString(R.string.unknown_author) } returns NO_AUTHOR
        every { stringsLoader.getString(R.string.error_generic) } returns GENERIC_ERROR

        every { repository.loadNextQuote(any()) } answers {
            QuoteResponse("Aphorism", "Author", "Sender", "Sender URL", "URL")
        } andThenThrows RuntimeException() andThenThrows NetworkException(R.string.error_network)

        presenter = MainScreenPresenter(stringsLoader, repository, Dispatchers.Unconfined, Dispatchers.Unconfined)

    }

    @Test
    fun attachView() {
        verify(exactly = 0) { view.renderState(any()) }
        presenter.attachView(view)
        verify(exactly = 1) { view.renderState(any()) }
    }

    @Test
    fun detachView() {
        presenter.detachView()
        presenter.attachView(view)

        verify(exactly = 1) { view.renderState(any()) }
        presenter.detachView()

        verify(exactly = 1) { view.renderState(any()) }
    }

    @Test
    fun loadNextQuote() = runBlocking {
        presenter.attachView(view)
        presenter.loadNextQuote()

        verify(exactly = 3) { view.renderState(any()) }

        presenter.loadNextQuote()
        verify(exactly = 5) { view.renderState(any()) }
        verify(exactly = 1) { view.displayError(GENERIC_ERROR) }

        presenter.loadNextQuote()
        verify(exactly = 7) { view.renderState(any()) }
        verify(exactly = 1) { view.displayError(NETWORK_ERROR) }
    }

    private companion object {
        const val WAIT = "Please wait"
        const val APP_AUTHORS = "App author"
        const val NO_AUTHOR = "no author"
        const val NETWORK_ERROR = "Network error"
        const val GENERIC_ERROR = "Generic error"
    }
}
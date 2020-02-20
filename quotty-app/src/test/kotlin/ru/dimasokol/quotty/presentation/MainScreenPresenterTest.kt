package ru.dimasokol.quotty.presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import ru.dimasokol.quotty.R
import ru.dimasokol.quotty.data.QuoteResponse
import ru.dimasokol.quotty.data.QuotesRepository
import ru.dimasokol.quotty.exceptions.NetworkException
import ru.dimasokol.quotty.utils.StringsLoader
import kotlin.coroutines.CoroutineContext

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
        every { stringsLoader.getString(R.string.app_authors) } returns AUTHORS
        every { stringsLoader.getString(R.string.error_network) } returns NETWORK_ERROR

        every { repository.loadNextQuote(any()) } answers {
            QuoteResponse("Aphorism", "Author", "Sender", "Sender URL", "URL")
        } andThenThrows RuntimeException() andThenThrows NetworkException(R.string.error_network)

        Dispatchers.setMain(newSingleThreadContext("UI"))

        presenter = MainScreenPresenter(stringsLoader, repository)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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
    @Ignore
    fun loadNextQuote() = runBlockingTest {
        presenter.attachView(view)
        presenter.loadNextQuote()

        verify(exactly = 3) { view.renderState(any()) }

        presenter.loadNextQuote()
        verify(exactly = 5) { view.renderState(any()) }
    }

    private companion object {
        const val WAIT = "Please wait"
        const val AUTHORS = "no author"
        const val NETWORK_ERROR = "Network error"
    }

    private object fakeDispatcher: CoroutineDispatcher() {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            block.run()
        }
    }
}
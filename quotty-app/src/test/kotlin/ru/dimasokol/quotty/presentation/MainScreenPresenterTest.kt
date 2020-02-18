package ru.dimasokol.quotty.presentation

import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import ru.dimasokol.quotty.R
import ru.dimasokol.quotty.data.QuoteResponse
import ru.dimasokol.quotty.data.QuotesRepository
import ru.dimasokol.quotty.utils.StringsLoader

class MainScreenPresenterTest {

    lateinit var view: MainScreenView
    lateinit var presenter: MainScreenPresenter
    lateinit var stringsLoader: StringsLoader
    lateinit var repository: QuotesRepository

    @Before
    fun setUp() {
        view = mock(MainScreenView::class.java)
        stringsLoader = mock(StringsLoader::class.java)
        repository = mock(QuotesRepository::class.java)

        `when`(stringsLoader.getString(R.string.please_wait)).thenReturn(WAIT);
        `when`(stringsLoader.getString(R.string.app_authors)).thenReturn(AUTHORS);

        `when`(repository.loadNextQuote(any()))
            .thenReturn(QuoteResponse("Aphorism", "Author", "Sender", "Sender URL", "URL"))
            .thenThrow(RuntimeException())

        presenter = MainScreenPresenter(stringsLoader, repository)
    }

    @Test
    fun attachView() {
        verify(view, never()).renderState(any())
        presenter.attachView(view)
        verify(view).renderState(any())
    }

    @Test
    fun detachView() {
        presenter.detachView()
        presenter.attachView(view)

        verify(view, times(1)).renderState(any())
        presenter.detachView()

        verify(view, times(1)).renderState(any())
    }

    @Test
    fun loadNextQuote() {
        presenter.attachView(view)

        runBlocking {
            presenter.loadNextQuote()
        }

        verify(view, times(3)).renderState(any())


        runBlocking {
            presenter.loadNextQuote()
        }
        verify(view, times(5)).renderState(any())
    }

    // Хренов котёл опять требует костылей
    private fun <T> any(): T = Mockito.any<T>()

    private companion object {
        const val WAIT = "Please wait"
        const val AUTHORS = "no author"
    }
}
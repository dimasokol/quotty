package ru.dimasokol.quotty.data

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.stubbing.Answer
import ru.dimasokol.quotty.network.UrlLoader
import ru.dimasokol.quotty.utils.Sleeper
import java.io.InputStream

class NetworkQuotesRepositoryTest {

    lateinit var networkLoader: UrlLoader
    lateinit var repository: NetworkQuotesRepository
    var counter: Int = 0

    @Before
    fun setUp() {
        networkLoader = mock(UrlLoader::class.java)
        counter = 0

        `when`(networkLoader.loadAsStream(any())).thenAnswer(Answer<InputStream> {
            counter++

            if (counter < 3) {
                return@Answer javaClass.classLoader?.getResourceAsStream("quote.json")
            }

            return@Answer javaClass.classLoader?.getResourceAsStream("quote2.json")
        })

        repository = NetworkQuotesRepository(networkLoader, mock(Sleeper::class.java))
    }

    @Test(timeout = 1000L)
    fun loadNextQuote() {
        var result = repository.loadNextQuote(null)
        assertEquals(OLD_URL, result.url)

        result = repository.loadNextQuote(OLD_URL)
        assertEquals(NEW_URL, result.url)
    }

    // Хренов котёл опять требует костылей
    private fun <T> any(): T = Mockito.any<T>()

    private companion object {
        const val OLD_URL = "http://forismatic.com/ru/7a84fd4f7f/"
        const val NEW_URL = "http://forismatic.com/ru/817ebb2a9d/"
    }
}
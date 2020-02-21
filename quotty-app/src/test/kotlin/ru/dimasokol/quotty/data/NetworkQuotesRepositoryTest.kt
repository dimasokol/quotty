package ru.dimasokol.quotty.data

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.dimasokol.quotty.exceptions.NetworkException
import ru.dimasokol.quotty.network.UrlLoader
import ru.dimasokol.quotty.utils.Sleeper
import java.io.BufferedInputStream
import java.net.UnknownHostException

class NetworkQuotesRepositoryTest {

    lateinit var networkLoader: UrlLoader
    lateinit var repository: NetworkQuotesRepository
    lateinit var sleeper: Sleeper
    var counter: Int = 0

    @Before
    fun setUp() {
        networkLoader = mockk()
        counter = 0

        every { networkLoader.loadAsStream(any()) } answers {
            BufferedInputStream(javaClass.classLoader?.getResourceAsStream("quote.json"))
        } andThen {
            BufferedInputStream(javaClass.classLoader?.getResourceAsStream("quote.json"))
        } andThen {
            BufferedInputStream(javaClass.classLoader?.getResourceAsStream("quote.json"))
        } andThen {
            BufferedInputStream(javaClass.classLoader?.getResourceAsStream("quote2.json"))
        }

        sleeper = mockk()
        every { sleeper.sleepFor(any()) } answers { nothing }

        repository = NetworkQuotesRepository(networkLoader, sleeper)
    }

    @Test(timeout = 1000L)
    fun loadNextQuote() {
        var result = repository.loadNextQuote(null)
        assertEquals(OLD_URL, result.url)

        result = repository.loadNextQuote(OLD_URL)
        assertEquals(NEW_URL, result.url)
    }

    @Test(expected = NetworkException::class)
    fun isRightExceptionThrown() {
        val loader = mockk<UrlLoader>()
        every { loader.loadAsStream(any()) } throws UnknownHostException()

        repository = NetworkQuotesRepository(loader, sleeper)
        repository.loadNextQuote(null)
    }


    private companion object {
        const val OLD_URL = "http://forismatic.com/ru/7a84fd4f7f/"
        const val NEW_URL = "http://forismatic.com/ru/817ebb2a9d/"
    }
}
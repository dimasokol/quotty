package ru.dimasokol.quotty.data

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.dimasokol.quotty.network.UrlLoader
import ru.dimasokol.quotty.utils.Sleeper
import java.io.BufferedInputStream

class NetworkQuotesRepositoryTest {

    lateinit var networkLoader: UrlLoader
    lateinit var repository: NetworkQuotesRepository
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

        val sleeper = mockk<Sleeper>()
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


    private companion object {
        const val OLD_URL = "http://forismatic.com/ru/7a84fd4f7f/"
        const val NEW_URL = "http://forismatic.com/ru/817ebb2a9d/"
    }
}
package ru.dimasokol.quotty.data

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert.assertEquals
import org.junit.Test

class QuoteResponseTest {

    @Test
    fun testParsing() {
        val stream = javaClass.classLoader?.getResourceAsStream("quote.json")
        val mapper = ObjectMapper()
        val parsed = mapper.readValue<QuoteResponse>(stream, QuoteResponse::class.java)

        assertEquals(TEXT, parsed.text)
        assertEquals(AUTHOR, parsed.author)
        assertEquals(SENDER, parsed.sender)
        assertEquals(SENDER_URL, parsed.senderLink)
        assertEquals(URL, parsed.url)
    }

    companion object {
        const val TEXT = "Они все могут, потому что уверены, что могут все. "
        const val AUTHOR = "Вергилий"
        const val URL = "http://forismatic.com/ru/7a84fd4f7f/"
        const val SENDER = "Sender"
        const val SENDER_URL = "http://ya.ru"
    }
}
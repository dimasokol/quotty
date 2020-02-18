package ru.dimasokol.quotty.data

interface QuotesRepository {

    fun loadNextQuote(lastId: String?): QuoteResponse

}
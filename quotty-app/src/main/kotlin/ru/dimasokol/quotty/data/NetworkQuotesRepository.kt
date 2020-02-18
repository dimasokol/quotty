package ru.dimasokol.quotty.data

import com.fasterxml.jackson.databind.ObjectMapper
import ru.dimasokol.quotty.R
import ru.dimasokol.quotty.exceptions.NetworkException
import ru.dimasokol.quotty.network.UrlLoader
import ru.dimasokol.quotty.utils.Sleeper
import java.io.IOException
import java.net.URL
import java.net.UnknownHostException

class NetworkQuotesRepository(private val loader: UrlLoader, private val sleeper: Sleeper) : QuotesRepository {

    override fun loadNextQuote(lastId: String?): QuoteResponse {
        var freshId = ""
        val mapper = ObjectMapper()
        val url = URL("https://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=ru")
        var response: QuoteResponse

        do {
            if (freshId.length > 0) {
                sleeper.sleepFor(500)
            }

            try {
                response = mapper.readValue(loader.loadAsStream(url), QuoteResponse::class.java)
                freshId = response.url
            } catch (noNetwork: UnknownHostException) {
                throw NetworkException(R.string.error_no_internet)
            } catch (io: IOException) {
                throw NetworkException(R.string.error_network)
            }

        } while (freshId == lastId)

        return response
    }
}
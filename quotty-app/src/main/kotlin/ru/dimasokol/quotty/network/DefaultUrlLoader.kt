package ru.dimasokol.quotty.network

import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DefaultUrlLoader : UrlLoader {

    override fun loadAsStream(url: URL): InputStream {
        val connection = url.openConnection() as HttpURLConnection

        // На некоторых API без таймаута мы навечно зависаем при включении полёта
        connection.readTimeout = READ_TIMEOUT
        return connection.inputStream
    }


    private companion object {
        const val READ_TIMEOUT = 60_000
    }
}
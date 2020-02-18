package ru.dimasokol.quotty.network

import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DefaultUrlLoader : UrlLoader {

    override fun loadAsStream(url: URL): InputStream {
        val connection = url.openConnection() as HttpURLConnection
        return connection.inputStream
    }

}
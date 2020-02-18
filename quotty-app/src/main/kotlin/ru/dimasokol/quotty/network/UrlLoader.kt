package ru.dimasokol.quotty.network

import java.io.InputStream
import java.net.URL

interface UrlLoader {

    fun loadAsStream(url: URL): InputStream

}
package ru.dimasokol.quotty.utils

abstract class Sleeper(private val timeSource: TimeSource) {

    abstract fun sleepFor(milliseconds: Long)

    fun sleepTo(unixTime: Long) {
        val time = timeSource.unixTime()
        val diff = unixTime - time

        if (diff > 0) {
            sleepFor(diff)
        }
    }

}
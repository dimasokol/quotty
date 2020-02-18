package ru.dimasokol.quotty.utils

class TimeSourceImpl : TimeSource {
    override fun unixTime(): Long = System.currentTimeMillis()
}
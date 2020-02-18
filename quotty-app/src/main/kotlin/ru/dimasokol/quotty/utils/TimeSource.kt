package ru.dimasokol.quotty.utils

interface TimeSource {
    fun unixTime(): Long
}
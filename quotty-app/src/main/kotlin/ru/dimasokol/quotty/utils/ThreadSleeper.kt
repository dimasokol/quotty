package ru.dimasokol.quotty.utils

class ThreadSleeper : Sleeper(TimeSourceImpl()) {
    override fun sleepFor(milliseconds: Long) {
        Thread.sleep(milliseconds)
    }
}
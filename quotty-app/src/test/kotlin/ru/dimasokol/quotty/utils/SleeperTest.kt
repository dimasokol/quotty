package ru.dimasokol.quotty.utils

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SleeperTest {

    lateinit var timeSource: TimeSource
    lateinit var sleeper: Sleeper

    @Before
    fun setUp() {
        timeSource = mockk()
        every {timeSource.unixTime()} returns CURRENT_TIME

        sleeper = spyk(MockSleeper(timeSource))
    }

    @Test
    fun sleepTo() {
        sleeper.sleepTo(PAST_TIME)
        verify(exactly = 0) { sleeper.sleepFor(any()) }

        sleeper.sleepTo(CURRENT_TIME)
        verify(exactly = 0) { sleeper.sleepFor(any()) }

        sleeper.sleepTo(FUTURE_TIME)
        verify(exactly = 1) { sleeper.sleepFor(eq(EXPECTED_SLEEP)) }
    }

    private open class MockSleeper(timeSource: TimeSource) : Sleeper(timeSource) {
        override fun sleepFor(milliseconds: Long) {
        }
    }

    companion object {
        const val EXPECTED_SLEEP = 666L
        const val CURRENT_TIME = 1000L
        const val FUTURE_TIME = 1666L
        const val PAST_TIME = 999L
    }
}
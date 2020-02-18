package ru.dimasokol.quotty.utils

import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.*

class SleeperTest {

    lateinit var timeSource: TimeSource
    lateinit var sleeper: Sleeper

    @Before
    fun setUp() {
        timeSource = mock(TimeSource::class.java)
        `when`(timeSource.unixTime()).thenReturn(CURRENT_TIME)

        sleeper = spy(MockSleeper(timeSource))
    }

    @Test
    fun sleepTo() {
        sleeper.sleepTo(PAST_TIME)
        verify(sleeper, never()).sleepFor(anyLong())

        sleeper.sleepTo(CURRENT_TIME)
        verify(sleeper, never()).sleepFor(anyLong())

        sleeper.sleepTo(FUTURE_TIME)
        verify(sleeper).sleepFor(eq(EXPECTED_SLEEP))
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
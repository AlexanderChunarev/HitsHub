package com.example.hitshub

import com.example.hitshub.models.Message
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MessageSelectorTest {
    private val messages = listOf(
        Message("user_name1", "", "", 1, 1),
        Message("user_name2", "", "", 1, 2),
        Message("user_name3", "", "", 1, 4),
        Message("user_name4", "", "", 1, 5),
        Message("user_name5", "", "", 1, 22),
        Message("user_name6", "", "", 1, 5),
        Message("user_name7", "", "", 1, 2),
        Message("user_name8", "", "", 1, 1),
        Message("user_name9", "", "", 1, 5),
        Message("user_name10", "", "", 1, 1)
    )
    private val list by lazy { mutableListOf<Message>() }
    private var expectedPosition = 0

    @Test
    fun selectNullMessages() {
        val sample = getMessages(111).takeIf { it.isNotEmpty() }?.random()
        assertNull(sample)
    }

    @Test
    fun selectMessagesInRangeFromOneToFourTest() {
        val messagesInRangeOneToFour = getMessages(1)
        assertEquals("user_name1", messagesInRangeOneToFour[0].name)
        assertEquals("user_name2", messagesInRangeOneToFour[1].name)
        assertEquals("user_name3", messagesInRangeOneToFour[2].name)
        assertEquals("user_name7", messagesInRangeOneToFour[3].name)
        assertEquals("user_name8", messagesInRangeOneToFour[4].name)
        assertEquals("user_name10", messagesInRangeOneToFour[5].name)
    }

    @Test
    fun test2() {
        get(1)
        assertEquals(3, list.size)
        assertEquals(1, list[0].time)
        assertEquals(4, list[1].time)
        assertEquals(19, list[2].time)
    }

    private fun get(playerCurrPos: Int) {
        expectedPosition = 0
        list.clear()
        for (playerTick in playerCurrPos..TRACK_DURATION) {
            if (expectedPosition == 0 || expectedPosition == playerTick) {
                val sample = getMessages(playerTick).takeIf { it.isNotEmpty() }?.random()
                sample?.let {
                    it.time = playerTick
                    list.add(sample)
                }
                expectedPosition = playerTick + 3
                if (expectedPosition > TRACK_DURATION) return
            }
        }
    }

    @Test
    fun selectMessagesWhilePlayerPlayingTest() = runBlocking {
        for (playerTick in 1..22) {
            delay(1000)
            messages.forEach {
                if (it.time == playerTick) {
                    if (expectedPosition == 0 || expectedPosition < playerTick) {
                        val sample = getMessages(playerTick)
                        expectedPosition = playerTick + 3
                        when (playerTick) {
                            1 -> assertEquals(6, sample.size)
                            5 -> assertEquals(3, sample.size)
                            22 -> assertEquals(1, sample.size)
                        }
                        println(sample.random())
                    }
                }
            }
        }
    }

    private fun getMessages(time: Int) =
        run { messages.filter { it.time <= time + 3 && it.time >= time } }

    companion object {
        private const val PLAYER_TIME_1 = 1
        private const val TRACK_DURATION = 30
        private const val RANGE = 3
        private const val TIMEOUT = 24
    }
}

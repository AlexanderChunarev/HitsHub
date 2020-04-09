package com.example.hitshub.utils

import com.example.hitshub.models.Message

class MessageSelector {
    private val messages by lazy { mutableListOf<Message>() }
    private var expectedPosition = 0
    val list by lazy { mutableListOf<Message>() }

    fun setMessages(messages: List<Message>) {
        this.messages.apply {
            clear()
            addAll(messages)
        }
        expectedPosition = 0
    }

    fun get(playerCurrPos: Int, duration: Int) {
        expectedPosition = 0
        list.clear()
        for (playerTick in playerCurrPos..duration) {
            if (expectedPosition == 0 || expectedPosition == playerTick) {
                val sample = getMessage(playerTick).takeIf { it.isNotEmpty() }?.random()
                sample?.let {
                    list.add(Message().apply {
                        name = it.name
                        avatarUrl = it.avatarUrl
                        content = it.content
                        time = playerTick
                    })
                }
                expectedPosition = playerTick + PAUSE
                if (expectedPosition > duration) return
            }
        }
    }

    private fun getMessage(time: Int) =
        run {
            messages.filter { it.time <= time + PAUSE && it.time >= time }
        }

    companion object {
        const val PAUSE = 3
        private var messageSelector: MessageSelector? = null
        fun getInstance(): MessageSelector {
            if (messageSelector == null) {
                messageSelector = MessageSelector()
            }
            return messageSelector!!
        }
    }
}

package com.example.hitshub.utils

sealed class MessageUtils {
    class ErrorMessage(val errorString: String) : MessageUtils()
}
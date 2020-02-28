package com.example.hitshub.utils

object ValidationUtils {

    fun isEmailValid(email: String) = Regex(pattern = EMAIL_REGEX).containsMatchIn(email)

    private const val EMAIL_REGEX =
        "^[\\w-]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$"

}
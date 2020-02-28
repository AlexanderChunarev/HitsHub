package com.example.hitshub.utils

object ValidationUtils {

    fun isEmailValid(email: String) = Regex(pattern = EMAIL_REGEX).containsMatchIn(email)

    fun isPasswordValid(password: String) = Regex(pattern = PASSWORD_REGEX).containsMatchIn(password)

    private const val EMAIL_REGEX =
        "^[\\w-]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$"

    private const val PASSWORD_REGEX =
        "^[\\w]+$"
}

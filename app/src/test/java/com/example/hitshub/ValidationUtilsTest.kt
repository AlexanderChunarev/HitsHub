package com.example.hitshub

import com.example.hitshub.utils.ValidationUtils
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ValidationUtilsTest {
    private val validEmails = listOf(
        "mkyong@yahoo.com",
        "mkyong100@yahoo.com", "mkyong.100@yahoo.com",
        "mkyong111@mkyong.com", "mkyong100@mkyong.net",
        "mkyong.100@mkyong.com.au", "mkyong@1.com",
        "mkyong@gmail.com.com", "mkyong100@gmail.com",
        "mkyong-100@yahoo-test.com"
    )

    private val invalidEmails = listOf(
        "mkyong", "mkyong@.com",
        "mkyong123@gmail.a", "mkyong123@.com", "mkyong123@.com.com",
        ".mkyong@mkyong.com", "mkyong()*@gmail.com", "mkyong@%*.com",
        "mkyong..2002@gmail.com", "mkyong.@gmail.com",
        "mkyong@mkyong@gmail.com", "mkyong@gmail.com.1a"
    )

    private val validPasswords = listOf(
        "testpassword",
        "1test1password22",
        "tEsTPasswoRD",
        "1tEsTP2asswoR33D"
    )

    private val invalidPasswords = listOf(
        "test password",
        ".testpassword",
        ".testpassword",
        "testpass%word",
        "#testpassword",
        "test-password"
    )

    @Test
    fun test_valid_email_validation() {
        validEmails.forEach {
            assertEquals(true, ValidationUtils.isEmailValid(it))
        }
    }

    @Test
    fun test_invalid_email_validation() {
        invalidEmails.forEach {
            assertEquals(false, ValidationUtils.isEmailValid(it))
        }
    }

    @Test
    fun test_valid_password() {
        validPasswords.forEach {
            assertEquals(true, ValidationUtils.isPasswordValid(it))
        }
    }

    @Test
    fun test_invalid_password() {
        invalidPasswords.forEach {
            assertEquals(false, ValidationUtils.isPasswordValid(it))
        }
    }
}

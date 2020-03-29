package com.example.hitshub.extentions

import java.lang.StringBuilder
import java.util.*
import java.util.concurrent.TimeUnit

const val HOURS = "hh"
const val MINUTES = "mm"
const val SECONDS = "ss"
private const val DIVIDER = ':'

fun Int.format(pattern: String): String {
    val result by lazy { StringBuilder() }
    pattern.replace("\\s".toRegex(), "").split(DIVIDER).forEach {
        when (it.toLowerCase(Locale.ROOT)) {
            HOURS -> result.append(TimeUnit.SECONDS.toHours(this.toLong())).append(DIVIDER)
            MINUTES -> result.append(TimeUnit.SECONDS.toMinutes(this.toLong())).append(DIVIDER)
            SECONDS -> result.append(TimeUnit.SECONDS.toSeconds(this.toLong()))
        }
    }
    return result.toString().trimEnd(DIVIDER)
}

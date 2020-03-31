package com.example.hitshub.extentions

fun <E> MutableList<E>.next(index: Int, track: E): E {
    val value = this.indexOf(track) + index
    return if (value >= 0 && value < this.size) this[value] else this[index]
}

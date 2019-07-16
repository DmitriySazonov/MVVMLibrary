package com.whenwhat.mvvmlibrary.other

fun <T> MutableList<T>.replaceOn(newItems: List<T>) = apply {
    clear()
    addAll(newItems)
}

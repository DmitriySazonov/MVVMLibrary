package com.whenwhat.framework.other

fun <T> MutableList<T>.replaceOn(newItems: List<T>) = apply {
    clear()
    addAll(newItems)
}

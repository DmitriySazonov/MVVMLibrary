package com.whenwhat.mvvmlibrary.property

import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

@Suppress("UNCHECKED_CAST")
fun <T> KProperty0<T>.tryBind(skipInitial: Boolean = false, receiver: (T) -> Unit) {
    isAccessible = true
    val delegate = (getDelegate() as? PropertyDelegate<T>) ?: return
    delegate.onValueChangeListener = receiver
    if (!skipInitial)
        receiver(delegate.value)
}
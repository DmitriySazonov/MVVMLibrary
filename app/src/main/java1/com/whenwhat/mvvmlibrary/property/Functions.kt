package com.whenwhat.mvvmlibrary.property

fun <T> property(initialValue: T, onChange: ((T) -> Unit)? = null) =
    PropertyDelegate(initialValue).apply {
        onValueChangeListener = onChange
    }
package com.whenwhat.mvvmlibrary.property

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PropertyDelegate<T>(initialValue: T) : ReadWriteProperty<Any?, T> {

    var onValueChangeListener: ((T) -> Unit)? = null

    var value = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
        onValueChangeListener?.invoke(value)
    }
}
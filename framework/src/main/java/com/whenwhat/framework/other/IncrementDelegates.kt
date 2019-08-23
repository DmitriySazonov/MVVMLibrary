package com.whenwhat.framework.other

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun intIncrement(initialValue: Int) = PropertyDelegate(initialValue) { it.inc() }
fun longIncrement(initialValue: Long) = PropertyDelegate(initialValue) { it.inc() }

class PropertyDelegate<T : Number>(initialValue: T, private val increment: (T) -> T) : ReadOnlyProperty<Any?, T> {

    private var value = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        value = increment(value)
        return value
    }
}
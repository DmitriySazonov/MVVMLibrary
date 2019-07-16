package com.whenwhat.mvvmlibrary.property

import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PropertyDelegate<T>(
    private val setter: (T) -> Unit,
    private val getter: () -> T
) : ReadWriteProperty<Any?, T> {

    val value: T
        get() = getter()

    private val onValueChangeListeners = LinkedList<((T) -> Unit)>()

    fun addOnValueChangeListener(listener: (T) -> Unit) {
        onValueChangeListeners += listener
    }

    fun removeOnValueChangeListener(listener: (T) -> Unit) {
        onValueChangeListeners -= listener
    }

    fun clearListeners() {
        onValueChangeListeners.clear()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        setter(value)
        onValueChangeListeners.forEach {
            it(value)
        }
    }
}
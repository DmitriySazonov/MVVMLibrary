package com.whenwhat.framework.property

import java.lang.ref.WeakReference
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class PropertyDelegate<T>(
    private val setter: (property: KProperty<*>, T) -> Unit,
    private val getter: (property: KProperty<*>) -> T
) : ReadWriteProperty<Any?, T> {

    private val onValueChangeListeners = LinkedList<WeakReference<(T) -> Unit>>()

    fun addOnValueChangeListener(listener: (T) -> Unit) {
        onValueChangeListeners += WeakReference(listener)
    }

    fun removeOnValueChangeListener(listener: (T) -> Unit) {
        onValueChangeListeners.removeAll {
            it.get() == listener
        }
    }

    fun clearListeners() {
        onValueChangeListeners.clear()
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getter(property)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        setter(property, value)
        onValueChangeListeners.forEach {
            it.get()?.invoke(value)
        }
    }
}
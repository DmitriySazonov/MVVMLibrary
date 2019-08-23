package com.whenwhat.framework.other

import java.util.*

class ObservableMap<K, V>(private val map: MutableMap<K, V> = TreeMap()) : MutableMap<K, V> by map {

    private var listener: ((Map<K, V>) -> Unit)? = null

    fun subscribeOnChange(listener: (Map<K, V>) -> Unit) {
        this.listener = listener
    }

    override fun put(key: K, value: V): V? = onChange { map.put(key, value) }

    override fun putAll(from: Map<out K, V>) = onChange { map.putAll(from) }

    override fun remove(key: K): V? = onChange { map.remove(key) }

    override fun clear() = onChange { map.clear() }

    private fun <T> onChange(action: () -> T): T {
        return action.invoke().also {
            listener?.invoke(map)
        }
    }
}
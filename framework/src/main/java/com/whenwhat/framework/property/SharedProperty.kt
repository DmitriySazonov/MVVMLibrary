package com.whenwhat.framework.property

import java.lang.ref.WeakReference
import kotlin.collections.set

private val sharedPropertyCache = HashMap<String, WeakReference<PropertyDelegate<*>>>()

@Suppress("UNCHECKED_CAST")
private fun <T> cachedSharedPropertyDelegate(key: String): PropertyDelegate<T>? {
    return sharedPropertyCache[key]?.get() as? PropertyDelegate<T>
}

@Suppress("UNCHECKED_CAST")
private fun cacheSharedPropertyDelegate(key: String, delegate: PropertyDelegate<*>) {
    sharedPropertyCache[key] = WeakReference(delegate)
}

fun <T> sharedProperty(name: String, defaultValue: T, onChange: ((T?) -> Unit)? = null): PropertyDelegate<T> {
    var value: T? = null
    return cachedSharedPropertyDelegate(name) ?: PropertyDelegate({ _, newValue ->
        value = newValue
    }, {
        value ?: defaultValue
    }).apply {
        if (onChange != null)
            addOnValueChangeListener(onChange)
        cacheSharedPropertyDelegate(name, this)
    }
}
package com.whenwhat.mvvmlibrary.property

import com.whenwhat.mvvmlibrary.storage.PropertyStorage
import com.whenwhat.mvvmlibrary.storage.get
import com.whenwhat.mvvmlibrary.storage.set
import java.lang.ref.WeakReference

private val sharedPropertyCache = HashMap<String, WeakReference<PropertyDelegate<*>>>()

fun <T> property(initialValue: T, onChange: ((T) -> Unit)? = null): PropertyDelegate<T> {
    var value: T = initialValue
    return PropertyDelegate({ value = it }, { value }).apply {
        if (onChange != null)
            addOnValueChangeListener(onChange)
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> cachedSharedPropertyDelegate(key: String): PropertyDelegate<T>? {
    return sharedPropertyCache[key]?.get() as? PropertyDelegate<T>
}

@Suppress("UNCHECKED_CAST")
fun cacheSharedPropertyDelegate(key: String, delegate: PropertyDelegate<*>) {
    sharedPropertyCache[key] = WeakReference(delegate)
}

inline fun <reified T : Any> sharedProperty(
    name: String,
    saved: Boolean = false,
    noinline onChange: ((T?) -> Unit)? = null
): PropertyDelegate<T?> {
    val key = "$name-${T::class.java.simpleName}"
    var value: T? = null
    return cachedSharedPropertyDelegate(key) ?: PropertyDelegate({
        if (saved)
            PropertyStorage[key] = it
        else
            value = it
    }, {
        if (saved)
            PropertyStorage[key]
        else
            value
    }).apply {
        if (onChange != null)
            addOnValueChangeListener(onChange)
        cacheSharedPropertyDelegate(key, this)
    }
}

inline fun <reified T : Any> sharedProperty(
    initialValue: T,
    name: String,
    saved: Boolean = false,
    noinline onChange: ((T) -> Unit)? = null
): PropertyDelegate<T> {
    val key = "$name-${T::class.java.simpleName}"
    var value: T = initialValue
    return cachedSharedPropertyDelegate(key) ?: PropertyDelegate({
        if (saved)
            PropertyStorage[key] = it
        else
            value = it
    }, {
        if (saved)
            PropertyStorage[key] ?: initialValue
        else
            value
    }).apply {
        if (onChange != null)
            addOnValueChangeListener(onChange)
        cacheSharedPropertyDelegate(key, this)
    }
}
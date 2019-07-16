package com.whenwhat.mvvmlibrary.other

import android.os.Bundle

val Bundle.objects: List<Any>
    get() = keySet().mapNotNull { get(it) }

@Suppress("UNCHECKED_CAST")
inline fun <reified T> Bundle.getObject(): T? {
    val clazz = T::class.java
    return objects.find { clazz.isInstance(it) } as? T
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> Bundle.allObjects(): List<T> {
    val clazz = T::class.java
    return objects.mapNotNull { param ->
        param.takeIf { clazz.isInstance(it) } as? T
    }
}

package com.whenwhat.mvvmlibrary.storage

import android.util.Log
import androidx.core.content.edit

operator fun KeyValueStorage.set(key: String, any: Any?) = edit(commit = true) {
    if (any == null) {
        remove(key)
        return@edit
    }
    when (any) {
        is Boolean -> putBoolean(key, any)
        is Int -> putInt(key, any)
        is Long -> putLong(key, any)
        is Float -> putFloat(key, any)
        is String -> putString(key, any)
        else -> IllegalArgumentException("Invalid parameter type")
    }
}

inline operator fun <reified T : Any> KeyValueStorage.get(key: String): T? {
    if (!contains(key))
        return null
    val value: Any? = when (T::class.javaObjectType) {
        Boolean::class.javaObjectType -> getBoolean(key, false)
        Int::class.javaObjectType -> getInt(key, -1)
        Long::class.javaObjectType -> getLong(key, -1)
        Float::class.javaObjectType -> getFloat(key, -1f)
        String::class.javaObjectType -> getString(key, null)
        else -> throw IllegalArgumentException("Invalid parameter type")
    }
    return value as? T
}

inline operator fun <reified T : Any> KeyValueStorage.get(key: String, default: T): T {
    return get(key) ?: default
}
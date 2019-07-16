package com.whenwhat.mvvmlibrary.navigation

import java.io.Serializable

class NavigationParam(vararg val params:  Serializable) : Serializable {

    companion object {
        val BUNDLE_KEY = "NavigationParam"
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> getObject(): T? {
        val clazz = T::class.java
        return params.find { clazz.isInstance(it) } as? T
    }

    override fun toString(): String {
        return params.joinToString { it.toString() }
    }
}
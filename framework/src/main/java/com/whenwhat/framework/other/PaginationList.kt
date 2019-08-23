package com.whenwhat.framework.other

import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty


abstract class PaginationList<T>(list: MutableList<T> = mutableListOf()) : DecoratedList<T>(list) {

    var hasError by notifyDelegate(false)
    var hasLoading by notifyDelegate(false)
    var endList by notifyDelegate(false)

    abstract fun createFooterError(): T?
    abstract fun createFooterLoader(): T?

    override fun footer(): T? {
        return when {
            endList -> null
            hasLoading -> createFooterLoader()
            hasError -> createFooterError()
            else -> null
        }
    }

    private fun <T> notifyDelegate(initialValue: T): ReadWriteProperty<Any?, T> {
        return Delegates.observable(initialValue) { _, oldValue, newValue ->
            if (oldValue != newValue) notifyDataChange()
        }
    }
}
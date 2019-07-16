package com.whenwhat.mvvmlibrary.property

import android.widget.TextView
import com.whenwhat.mvvmlibrary.other.ObservableList
import com.whenwhat.mvvmlibrary.other.Subscription
import com.whenwhat.mvvmlibrary.recyclerview.RecyclerAdapter
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

@Suppress("UNCHECKED_CAST")
fun <T> KProperty0<T>.tryBind(skipInitial: Boolean = false, receiver: (T) -> Unit): Subscription? {
    isAccessible = true
    val delegate = (getDelegate() as? PropertyDelegate<T>) ?: return null
    delegate.addOnValueChangeListener(receiver)
    if (!skipInitial)
        receiver(delegate.value)
    return object : Subscription {
        override fun unSubscribe() {
            delegate.removeOnValueChangeListener(receiver)
        }
    }
}

fun <T> KProperty0<List<T>>.tryBind(adapter: RecyclerAdapter<T>, skipInitial: Boolean = false): Subscription? {
    return tryBind(skipInitial = skipInitial) {
        adapter.setItems(it)
    }
}

fun <T> ObservableList<T>.tryBindAsSource(adapter: RecyclerAdapter<T>, skipInitial: Boolean = false): Subscription? {
    if (!skipInitial)
        adapter.setItems(this)
    return subscribeOnChange { _, newList ->
        adapter.setItems(newList)
    }
}

fun KProperty0<Any?>.tryBindAsText(textView: TextView, skipInitial: Boolean = false): Subscription? {
    return tryBind(skipInitial = skipInitial) {
        textView.text = it?.toString()
    }
}
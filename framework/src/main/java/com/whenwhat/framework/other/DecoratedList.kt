package com.whenwhat.framework.other

import java.util.*

abstract class DecoratedList<T>(list: MutableList<T> = mutableListOf()) : ObservableList<T>(list) {

    val decoratedList: List<T>
        get() = buildDecoratedList()
    private val subscribers = mutableListOf<(newList: List<T>) -> Unit>()

    init {
        subscribeOnChange { _, _ ->
            notifyDataChange()
        }
    }

    open fun header(): T? = null
    open fun footer(): T? = null
    open fun dividerBetween(beforeItem: T, afterItem: T): T? = null

    fun subscribeOnDecoratedListChange(action: (newList: List<T>) -> Unit): Subscription {
        subscribers += action
        return object : Subscription {
            override fun unSubscribe() {
                subscribers -= action
            }
        }
    }

    fun notifyDataChange() {
        val decoratedList = decoratedList
        subscribers.forEach { it(decoratedList) }
    }

    fun unSubscribeDecoratedList(action: (newList: List<T>) -> Unit) {
        subscribers -= action
    }

    override fun clearSubscribers() {
        super.clearSubscribers()
        subscribers.clear()
    }

    private fun buildDecoratedList(): List<T> {
        return LinkedList<T>().also { list ->
            list.addIfNotNull(header())
            var prevision: T? = null
            forEach {
                if (prevision != null)
                    list.addIfNotNull(dividerBetween(prevision!!, it))
                list.add(it)
                prevision = it
            }
            list.addIfNotNull(footer())
        }
    }

    private fun MutableList<T>.addIfNotNull(item: T?) {
        if (item != null)
            add(item)
    }
}
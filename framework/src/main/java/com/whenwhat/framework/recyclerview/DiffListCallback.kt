package com.whenwhat.framework.recyclerview

import androidx.recyclerview.widget.DiffUtil

class DiffListCallback<T>(private val compareItems: (T, T) -> Boolean, private val compareContents: (T, T) -> Boolean) : DiffUtil.Callback() {

    var oldList = emptyList<T>()
    var newList = emptyList<T>()

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            compareItems(oldList[oldItemPosition], newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            compareContents(oldList[oldItemPosition], newList[newItemPosition])

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size
}
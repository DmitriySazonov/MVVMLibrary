package com.whenwhat.mvvmlibrary.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.whenwhat.mvvmlibrary.other.ObservableList
import com.whenwhat.mvvmlibrary.other.replaceOn

abstract class RecyclerAdapter<ITEM>(items: List<ITEM> = emptyList()) : RecyclerView.Adapter<DefaultViewHolder>() {

    interface ItemBinder<ITEM> {
        fun createView(inflater: Inflater): View
        fun bind(holder: DefaultViewHolder, item: ITEM)
    }

    interface Inflater {
        val context: Context
        fun inflate(@LayoutRes layout: Int): View
    }

    val isEmpty
        get() = itemCount == 0

    private var inflater: Inflater? = null
    protected val items = ObservableList(mutableListOf<ITEM>())

    private val diffListCallback = DiffListCallback(::compareItems, ::compareContents)
    private val mainThreadHandler = Handler(Looper.getMainLooper())

    // viewType to Binder
    @SuppressLint("UseSparseArrays")
    private val binders = HashMap<Int, ItemBinder<Any>>()

    init {
        this.items.addAll(items)
        this.items.subscribeOnChange { oldList, newList ->
            notifyItems(oldList, newList)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> registerBinder(type: Class<out T>, binder: ItemBinder<T>) {
        binders[type.hashCode()] = binder as? ItemBinder<Any> ?: return
    }

    fun setItems(items: List<ITEM>) {
        this.items.transaction {
            replaceOn(items)
        }
    }

    fun addItems(items: List<ITEM>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun replaceItem(item: ITEM, finder: (ITEM) -> Boolean = { compareItems(item, it) }) {
        (items.indexOfFirst(finder).takeIf { it >= 0 } ?: return).let { position ->
            items.transaction {
                removeAt(position)
                add(position, item)
            }
            notifyItemChanged(position)
        }
    }

    fun removeItem(item: ITEM, finder: (ITEM) -> Boolean = { compareItems(item, it) }) {
        (items.indexOfFirst(finder).takeIf { it >= 0 } ?: return).apply {
            items.removeAt(this)
            notifyItemRemoved(this)
        }
    }

    fun clearItems() {
        this.items.clear()
        notifyDataSetChanged()
    }

    /**
     * use instead getItemViewType
     * */
    open fun getItemType(position: Int): Int {
        return super.getItemViewType(position)
    }

    abstract fun onBind(holder: DefaultViewHolder, item: ITEM)

    abstract fun createView(inflater: Inflater, viewType: Int): View

    final override fun getItemViewType(position: Int): Int {
        return items[position]?.let {
            (it as? Any)?.javaClass.hashCode()
        }?.takeIf {
            binders.containsKey(it)
        } ?: getItemType(position)
    }

    final override fun onBindViewHolder(holder: DefaultViewHolder, position: Int) {
        val item = items[position]
        binders[holder.itemViewType]?.bind(holder, items[position] as Any) ?: onBind(holder, item)
    }

    final override fun getItemCount(): Int = items.size

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultViewHolder {
        if (inflater == null)
            inflater = createInflater(parent)
        return DefaultViewHolder(binders[viewType]?.createView(inflater!!)
            ?: createView(inflater!!, viewType))
    }

    protected open fun compareItems(item1: ITEM, item2: ITEM) =
        item1 == item2

    protected open fun compareContents(item1: ITEM, item2: ITEM) =
        item1 == item2

    protected val DefaultViewHolder.isLast
        get() = layoutPosition + 1 == itemCount

    protected val DefaultViewHolder.numberPosition
        get() = layoutPosition + 1

    protected open fun notifyItems(oldList: List<ITEM>, newList: List<ITEM>) {
        diffListCallback.oldList = oldList
        diffListCallback.newList = newList
        try {
            DiffUtil.calculateDiff(diffListCallback).dispatchUpdatesTo(this)
        } catch (e: IllegalStateException) {
            mainThreadHandler.post { notifyItems(oldList, newList) }
        }
    }

    private fun createInflater(parent: ViewGroup) = object : Inflater {
        override val context: Context = parent.context
        override fun inflate(layout: Int): View =
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
    }
}
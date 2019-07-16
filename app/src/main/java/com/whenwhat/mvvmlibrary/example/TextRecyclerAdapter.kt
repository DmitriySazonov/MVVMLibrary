package com.whenwhat.mvvmlibrary.example

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.whenwhat.mvvmlibrary.recyclerview.DefaultViewHolder
import com.whenwhat.mvvmlibrary.recyclerview.RecyclerAdapter

class TextRecyclerAdapter : RecyclerAdapter<String>() {

    override fun onBind(holder: DefaultViewHolder, item: String) {
        (holder.itemView as TextView).text = item
    }

    override fun createView(inflater: Inflater, viewType: Int): View {
        return TextView(inflater.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }
}
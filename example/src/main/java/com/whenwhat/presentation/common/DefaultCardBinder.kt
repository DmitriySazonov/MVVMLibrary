package com.whenwhat.presentation.common

import android.view.View
import com.whenwhat.R
import com.whenwhat.framework.recyclerview.DefaultViewHolder
import com.whenwhat.framework.recyclerview.RecyclerAdapter
import kotlinx.android.synthetic.main.item_card.view.*

interface CardInfo {
    val image: Int
    val title: String
    val body: String
}

class DefaultCardBinder<T : CardInfo>(val clickListener: (T) -> Unit) : RecyclerAdapter.ItemBinder<T> {

    override fun createView(inflater: RecyclerAdapter.Inflater): View = inflater.inflate(R.layout.item_card)

    override fun bind(holder: DefaultViewHolder, item: T) {
        holder.itemView.apply {
            image.setImageResource(item.image)
            title.text = item.title
            body.text = item.body

            setOnClickListener {
                clickListener(item)
            }
        }
    }

}
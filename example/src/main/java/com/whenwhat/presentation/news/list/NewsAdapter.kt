package com.whenwhat.presentation.news.list

import android.view.View
import com.whenwhat.framework.recyclerview.DefaultViewHolder
import com.whenwhat.framework.recyclerview.RecyclerAdapter
import com.whenwhat.presentation.common.DefaultCardBinder

class NewsAdapter : RecyclerAdapter<NewsCard>() {

    var onNewsClick: ((NewsCard) -> Unit)? = null

    init {
        registerBinder(NewsCard::class.java, DefaultCardBinder { onNewsClick?.invoke(it) })
    }

    override fun onBind(holder: DefaultViewHolder, item: NewsCard) {
        throw IllegalArgumentException("not expect")
    }

    override fun createView(inflater: Inflater, viewType: Int): View {
        throw IllegalArgumentException("not expect")
    }
}
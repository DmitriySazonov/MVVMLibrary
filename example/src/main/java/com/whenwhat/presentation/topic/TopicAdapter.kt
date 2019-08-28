package com.whenwhat.presentation.topic

import android.view.View
import com.whenwhat.framework.recyclerview.DefaultViewHolder
import com.whenwhat.framework.recyclerview.RecyclerAdapter
import com.whenwhat.presentation.common.DefaultCardBinder

class TopicAdapter : RecyclerAdapter<TopicCard>() {

    var onTopicClick: ((TopicCard) -> Unit)? = null

    init {
        registerBinder(TopicCard::class.java, DefaultCardBinder { onTopicClick?.invoke(it) })
    }

    override fun onBind(holder: DefaultViewHolder, item: TopicCard) {
        throw IllegalArgumentException("not expect")
    }

    override fun createView(inflater: Inflater, viewType: Int): View {
        throw IllegalArgumentException("not expect")
    }
}
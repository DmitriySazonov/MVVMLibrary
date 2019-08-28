package com.whenwhat.data

import com.whenwhat.R
import com.whenwhat.data.models.News
import com.whenwhat.data.models.NewsDetail
import com.whenwhat.data.models.Topic

object RandomDataSource {

    val topics = (0..10).map(::defaultTopic)

    fun getTopic(topicId: Int): Topic {
        return defaultTopic(topicId)
    }

    fun getNews(topicId: Int): List<News> {
        return (0..10).map(::defaultNews)
    }

    fun getNewsDetail(newsId: Int): NewsDetail {
        return defaultNewsDetail(newsId)
    }

    private fun defaultNewsDetail(id: Int): NewsDetail {
        return NewsDetail(
                id = id,
                image = R.drawable.ic_android,
                title = "News title $id",
                description = "Long description $id"
        )
    }

    private fun defaultNews(id: Int): News {
        return News(
                id = id,
                image = R.drawable.ic_android,
                title = "News title $id",
                shortDescription = "Short description $id"
        )
    }

    private fun defaultTopic(id: Int): Topic {
        return Topic(
                id = id,
                image = R.drawable.ic_android,
                name = "Topic name $id",
                description = "Some description #$id"
        )
    }
}
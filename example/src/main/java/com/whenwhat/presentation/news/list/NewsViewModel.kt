package com.whenwhat.presentation.news.list

import com.whenwhat.data.RandomDataSource
import com.whenwhat.framework.other.ObservableList
import com.whenwhat.framework.property.property
import com.whenwhat.framework.property.sharedPreferenceProperty
import com.whenwhat.framework.viewmodel.MVVMViewModel

class NewsViewModel(private val topicId: Int) : MVVMViewModel() {

    val news = ObservableList<NewsCard>()
    var lastNewsId by sharedPreferenceProperty<Int?>("lastNewsId", null)
    var topicName by property("")
        private set

    override fun onFirstCreate() {
        super.onFirstCreate()

        topicName = RandomDataSource.getTopic(topicId).name

        RandomDataSource.getNews(topicId).map {
            NewsCard(
                    id = it.id,
                    image = it.image,
                    title = it.title,
                    body = it.shortDescription
            )
        }.also {
            news.addAll(it)
        }
    }
}
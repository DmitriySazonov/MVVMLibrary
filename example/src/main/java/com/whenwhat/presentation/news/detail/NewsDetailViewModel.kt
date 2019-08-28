package com.whenwhat.presentation.news.detail

import com.whenwhat.data.RandomDataSource
import com.whenwhat.data.models.NewsDetail
import com.whenwhat.framework.property.property
import com.whenwhat.framework.viewmodel.MVVMViewModel

class NewsDetailViewModel(private val newsId: Int) : MVVMViewModel() {

    var newsDetail by property<NewsDetail?>(null)
        private set

    override fun onFirstCreate() {
        super.onFirstCreate()

        newsDetail = RandomDataSource.getNewsDetail(newsId)
    }
}
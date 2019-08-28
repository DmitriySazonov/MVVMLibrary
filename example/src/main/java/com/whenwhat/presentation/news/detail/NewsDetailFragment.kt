package com.whenwhat.presentation.news.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whenwhat.R
import com.whenwhat.framework.property.tryBind
import com.whenwhat.framework.view.MVVMFragment
import kotlinx.android.synthetic.main.item_card.*

class NewsDetailFragment : MVVMFragment<NewsDetailViewModel>() {

    override fun createViewModel(): NewsDetailViewModel {
        return NewsDetailViewModel(getPrevScreenParam<NewsDetailParam>()!!.newsId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news_detail, container, false)
    }

    override fun bindViewModel(viewModel: NewsDetailViewModel) {
        super.bindViewModel(viewModel)

        -viewModel::newsDetail.tryBind {
            it ?: return@tryBind
            activity?.title = it.title
            image.setImageResource(it.image)
            body.text = it.description
        }
    }
}
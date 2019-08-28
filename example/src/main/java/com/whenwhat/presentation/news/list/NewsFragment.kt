package com.whenwhat.presentation.news.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.whenwhat.R
import com.whenwhat.ScreenFlow
import com.whenwhat.framework.property.tryBind
import com.whenwhat.framework.property.tryBindAsSource
import com.whenwhat.framework.view.MVVMFragment
import com.whenwhat.presentation.news.detail.NewsDetailParam
import kotlinx.android.synthetic.main.default_list.*
import java.io.Serializable

class NewsFragment : MVVMFragment<NewsViewModel>() {

    private val adapter = NewsAdapter()

    override fun createViewModel(): NewsViewModel {
        return NewsViewModel(getPrevScreenParam<NewsParam>()!!.topicId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.default_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.onNewsClick = ::onNewsClick
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun bindViewModel(viewModel: NewsViewModel) {
        super.bindViewModel(viewModel)

        -viewModel.news.tryBindAsSource(adapter)
        -viewModel::topicName.tryBind {
            activity?.title = it
        }
    }

    override fun onForwardScreenResult(result: Serializable) {
        super.onForwardScreenResult(result)

        Toast.makeText(context, result.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun onNewsClick(news: NewsCard) {
        viewModel?.lastNewsId = news.id
        navigator?.navigate(ScreenFlow.NEWS.DETAIL, NewsDetailParam(news.id))
    }
}
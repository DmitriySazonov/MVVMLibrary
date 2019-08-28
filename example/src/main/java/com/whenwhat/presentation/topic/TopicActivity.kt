package com.whenwhat.presentation.topic

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.whenwhat.R
import com.whenwhat.ScreenFlow
import com.whenwhat.framework.navigation.NavigationAction
import com.whenwhat.framework.property.tryBindAsSource
import com.whenwhat.framework.view.MVVMActivity
import com.whenwhat.presentation.news.detail.NewsDetailParam
import com.whenwhat.presentation.news.list.NewsParam
import kotlinx.android.synthetic.main.default_list.*
import java.io.Serializable

class TopicActivity : MVVMActivity<TopicViewModel>() {

    private val adapter = TopicAdapter()

    override fun createViewModel(): TopicViewModel {
        return TopicViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.default_list)

        adapter.onTopicClick = ::onTopicClick
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onForwardScreenResult(result: Serializable) {
        super.onForwardScreenResult(result)

        Toast.makeText(this, "$result", Toast.LENGTH_SHORT).show()
    }

    override fun bindViewModel(viewModel: TopicViewModel) {
        super.bindViewModel(viewModel)
        viewModel.topics.tryBindAsSource(adapter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_topics, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.last) {
            tryNavigateToLastTopic()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tryNavigateToLastTopic() {
        val newsId = viewModel?.lastNewsId
        if (newsId != null)
            navigator?.navigate(ScreenFlow.NEWS.DETAIL, NewsDetailParam(newsId))
        else
            Toast.makeText(this, "Not yet opened news", Toast.LENGTH_SHORT).show()

    }

    private fun onTopicClick(topicCard: TopicCard) {
        val navigationAction = ScreenFlow.NEWS.asNavigationAction(NewsParam(topicCard.id))
        navigator?.navigate(navigationAction)
    }
}

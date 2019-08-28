package com.whenwhat

import com.whenwhat.framework.navigation.point.ActivityNavigationPoint
import com.whenwhat.framework.navigation.point.Empty
import com.whenwhat.framework.navigation.point.HostActivityNavigationPoint
import com.whenwhat.presentation.news.NewsHostActivity
import com.whenwhat.presentation.news.detail.NewsDetailFragment
import com.whenwhat.presentation.news.detail.NewsDetailParam
import com.whenwhat.presentation.news.list.NewsFragment
import com.whenwhat.presentation.news.list.NewsParam
import com.whenwhat.presentation.topic.TopicActivity

object ScreenFlow {
    val NEWS = News
    val TOPICS = ActivityNavigationPoint<Empty>("TopicActivity", TopicActivity::class.java)
}

object News : HostActivityNavigationPoint<NewsParam>("NewsHostActivity", NewsHostActivity::class.java) {
    val LIST = rootFragmentPoint("NewsFragment", NewsFragment::class.java)
    val DETAIL = fragmentPoint<NewsDetailParam>("NewsDetailFragment", NewsDetailFragment::class.java)
}
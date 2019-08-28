package com.whenwhat.presentation.news.list

import com.whenwhat.presentation.common.CardInfo

data class NewsCard(
        val id: Int,
        override val image: Int,
        override val title: String,
        override val body: String
) : CardInfo
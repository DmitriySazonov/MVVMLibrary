package com.whenwhat.presentation.topic

import com.whenwhat.presentation.common.CardInfo

data class TopicCard(
        val id: Int,
        override val image: Int,
        override val title: String,
        override val body: String
) : CardInfo
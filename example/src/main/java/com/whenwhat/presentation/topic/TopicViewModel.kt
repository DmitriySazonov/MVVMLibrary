package com.whenwhat.presentation.topic

import com.whenwhat.data.RandomDataSource
import com.whenwhat.framework.other.ObservableList
import com.whenwhat.framework.property.sharedPreferenceProperty
import com.whenwhat.framework.viewmodel.MVVMViewModel

class TopicViewModel : MVVMViewModel() {

    val topics = ObservableList<TopicCard>()
    var lastNewsId by sharedPreferenceProperty<Int?>("lastNewsId", null)

    override fun onFirstCreate() {
        super.onFirstCreate()

        RandomDataSource.topics.map {
            TopicCard(
                id = it.id,
                image = it.image,
                title = it.name,
                body = it.description
            )
        }.also {
            topics.addAll(it)
        }
    }
}
package com.whenwhat.mvvmlibrary.example.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.whenwhat.mvvmlibrary.example.ScreenFlow
import com.whenwhat.mvvmlibrary.navigation.point.PointMetaData
import com.whenwhat.mvvmlibrary.view.MVVMFragment

class FirstFragment : MVVMFragment<FirstViewModel>() {

    override fun createViewModel(): FirstViewModel = FirstViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container ?: return null
        return TextView(container.context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            text = "FirstFragment-${hashCode()}"

            text = getPrevScreenParam<PointMetaData>().toString()

            setOnClickListener {
                navigator.navigate(ScreenFlow.SECOND_ACTIVITY.FIRST_FRAGMENT, 9)
            }
        }
    }
}
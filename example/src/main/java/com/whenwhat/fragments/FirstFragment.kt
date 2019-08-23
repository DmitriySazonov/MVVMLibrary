package com.whenwhat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whenwhat.ParamString
import com.whenwhat.R
import com.whenwhat.ScreenFlow
import com.whenwhat.TimeMeasure
import com.whenwhat.framework.property.tryBind
import com.whenwhat.framework.view.MVVMFragment
import kotlinx.android.synthetic.main.fragment_first.*
import java.io.Serializable

class FirstFragment : MVVMFragment<FirstViewModel>() {

    override fun createViewModel(): FirstViewModel = FirstViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun bindViewModel(viewModel: FirstViewModel) {
        super.bindViewModel(viewModel)

        -viewModel::text.tryBind {
            TimeMeasure.stop("text")
            firstLabel.text = it
        }

        action.setOnClickListener {
            navigator?.navigate(ScreenFlow.SECOND_ACTIVITY.INPUT_FRAGMENT, ParamString(""))
        }
    }

    override fun onForwardScreenResult(result: Serializable) {
        super.onForwardScreenResult(result)
        TimeMeasure.start("text")
        viewModel?.text = result.toString()
        TimeMeasure.start("text2")
        viewModel?.text2 = result.toString()
    }
}
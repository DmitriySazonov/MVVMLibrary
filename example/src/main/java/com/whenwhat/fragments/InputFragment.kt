package com.whenwhat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.whenwhat.ParamString
import com.whenwhat.R
import com.whenwhat.ScreenFlow
import com.whenwhat.framework.view.MVVMFragment
import kotlinx.android.synthetic.main.fragment_input.*

class InputFragment : MVVMFragment<InputViewModel>() {

    override fun createViewModel(): InputViewModel = InputViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forward.setOnClickListener {
            navigator?.navigate(
                ScreenFlow.SECOND_ACTIVITY.INPUT_FRAGMENT,
                ParamString(editingText.text.toString())
            )
        }
        back.setOnClickListener {
            navigator?.backWith(editingText.text.toString())
        }

        prevScreenData.text = getPrevScreenParam<ParamString>()?.param

        editingText.setText(getPrevScreenParam<ParamString>()?.param.toString())
    }
}
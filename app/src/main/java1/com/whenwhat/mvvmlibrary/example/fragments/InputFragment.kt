package com.whenwhat.mvvmlibrary.example.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.whenwhat.mvvmlibrary.R
import com.whenwhat.mvvmlibrary.example.ParamString
import com.whenwhat.mvvmlibrary.example.ScreenFlow
import com.whenwhat.mvvmlibrary.navigation.Navigator
import com.whenwhat.mvvmlibrary.navigation.point.PointMetaData
import com.whenwhat.mvvmlibrary.view.MVVMFragment
import kotlinx.android.synthetic.main.fragment_input.*
import java.io.Serializable

class InputFragment : MVVMFragment<InputViewModel>() {

    override fun createViewModel(): InputViewModel = InputViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forward.setOnClickListener {
            navigator.navigate(ScreenFlow.SECOND_ACTIVITY.INPUT_FRAGMENT, ParamString(editingText.text.toString()))
        }
        back.setOnClickListener {
            navigator.navigateBackWith(editingText.text.toString())
        }

        prevScreenData.text = getPrevScreenParam<PointMetaData>().toString()
        prevScreenData.setOnClickListener {
            Navigator.printNavigationStack()
        }

        editingText.setText(getPrevScreenParam<ParamString>()?.param.toString())
    }

    override fun onForwardScreenResult(data: Serializable) {
        super.onForwardScreenResult(data)
        Toast.makeText(context, data as String, Toast.LENGTH_LONG).show()
    }

}
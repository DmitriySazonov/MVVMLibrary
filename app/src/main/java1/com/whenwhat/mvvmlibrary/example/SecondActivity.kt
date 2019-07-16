package com.whenwhat.mvvmlibrary.example

import android.os.Bundle
import com.whenwhat.mvvmlibrary.R
import com.whenwhat.mvvmlibrary.view.MVVMHostActivity
import com.whenwhat.mvvmlibrary.viewmodel.MVVMHostViewModel

class SecondActivity : MVVMHostActivity<MVVMHostViewModel>() {

    override fun getContainerId(): Int = R.id.container

    override fun createViewModel() = object : MVVMHostViewModel(){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }
}

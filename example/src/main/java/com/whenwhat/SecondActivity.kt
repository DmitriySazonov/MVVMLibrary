package com.whenwhat

import android.os.Bundle
import com.whenwhat.framework.view.MVVMHostActivity
import com.whenwhat.framework.viewmodel.MVVMHostViewModel

class SecondActivity : MVVMHostActivity<MVVMHostViewModel>() {

    override fun getContainerId(): Int = R.id.container

    override fun createViewModel() = object : MVVMHostViewModel() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }
}

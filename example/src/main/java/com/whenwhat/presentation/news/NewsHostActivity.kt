package com.whenwhat.presentation.news

import android.os.Bundle
import com.whenwhat.R
import com.whenwhat.framework.view.MVVMHostActivity
import com.whenwhat.framework.viewmodel.MVVMViewModel

class NewsHostActivity : MVVMHostActivity<MVVMViewModel>() {

    override fun getContainerId(): Int = R.id.container

    override fun createViewModel() = object : MVVMViewModel() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.default_host_activity)
    }
}

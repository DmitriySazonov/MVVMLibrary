package com.whenwhat.framework.view

import android.os.Bundle
import com.whenwhat.framework.navigation.navigator.Navigator
import com.whenwhat.framework.viewmodel.MVVMViewModel

interface MVVMView<VM : MVVMViewModel> {

    val screenCode: Int
    val viewModel: VM?
    val navigator: Navigator?
    val isCreated: Boolean

    fun onCreate(delegate: MVVMViewDelegate<VM>, savedInstanceState: Bundle?)
    fun onSaveInstanceState(delegate: MVVMViewDelegate<VM>, outState: Bundle)
    fun onDestroy(delegate: MVVMViewDelegate<VM>, isChangingConfigurations: Boolean)
}
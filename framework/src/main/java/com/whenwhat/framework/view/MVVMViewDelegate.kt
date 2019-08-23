package com.whenwhat.framework.view

import com.whenwhat.framework.navigation.navigator.Navigator
import com.whenwhat.framework.viewmodel.MVVMViewModel

interface MVVMViewDelegate<VM : MVVMViewModel> {

    // navigation
    fun createNavigator(): Navigator

    // view model
    fun getViewModelLifeType(): ViewModelLifeType = ViewModelLifeType.LOCAL
    fun getViewModelTag(): String = "default"
    fun createViewModel(): VM
}
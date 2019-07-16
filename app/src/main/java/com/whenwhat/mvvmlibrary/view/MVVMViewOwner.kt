package com.whenwhat.mvvmlibrary.view

import com.whenwhat.mvvmlibrary.viewmodel.MVVMViewModel

interface MVVMViewOwner<VM : MVVMViewModel> {

    fun getViewModelLifeType(): ViewModelLifeType = ViewModelLifeType.LOCAL
    fun createViewModel(): VM
}
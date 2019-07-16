package com.whenwhat.mvvmlibrary.view

import android.os.Bundle

interface MVVMViewLifeCycle {
    fun onCreate(savedInstanceState: Bundle?)
    fun onSaveInstanceState(outState: Bundle)
    fun onDestroy(isChangingConfigurations: Boolean)
}
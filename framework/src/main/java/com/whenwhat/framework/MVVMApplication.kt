package com.whenwhat.framework

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

abstract class MVVMApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}
package com.whenwhat.mvvmlibrary.storage

import android.content.Context
import android.content.SharedPreferences
import com.whenwhat.mvvmlibrary.MVVMApplication

open class KeyValueStorage(private val name: String) : SharedPreferences by MVVMApplication.context.getSharedPreferences(name, Context.MODE_PRIVATE)
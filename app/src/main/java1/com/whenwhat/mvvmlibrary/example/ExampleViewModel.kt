package com.whenwhat.mvvmlibrary.example

import com.whenwhat.mvvmlibrary.viewmodel.MVVMViewModel
import com.whenwhat.mvvmlibrary.property.PropertyDelegate
import com.whenwhat.mvvmlibrary.property.property
import java.io.Serializable

class ExampleViewModel : MVVMViewModel() {

    var counter by property(1)
        private set

    var a by property("jn")

    override fun onCreate(state: Serializable?) {
        super.onCreate(state)
        counter = state as? Int ?: counter
    }

    override fun onSaveState(): Serializable? {
        return counter
    }

    fun increase() {
        counter++
    }

    fun decrease() {
        counter--
    }
}
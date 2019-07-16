package com.whenwhat.mvvmlibrary.example

import com.whenwhat.mvvmlibrary.other.ObservableList
import com.whenwhat.mvvmlibrary.property.sharedProperty
import com.whenwhat.mvvmlibrary.viewmodel.MVVMViewModel
import java.io.Serializable
import java.lang.Math.random

class ExampleViewModel : MVVMViewModel() {

    var counter by sharedProperty(1, "counter", saved = true)
        private set

    var counter2 by sharedProperty(1, "counter", saved = true)
        private set

    val elements = ObservableList<String>()

    override fun onCreate(state: Serializable?) {
        super.onCreate(state)
        counter = state as? Int ?: counter
    }

    override fun onSaveState(): Serializable? {
        return counter
    }

    fun increase() {
        counter++
        elements += random().toString()
    }

    fun decrease() {
        counter--
        elements -= elements.random()
    }
}
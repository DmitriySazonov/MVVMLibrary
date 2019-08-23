package com.whenwhat

import com.whenwhat.framework.other.ObservableList
import com.whenwhat.framework.property.sharedProperty
import com.whenwhat.framework.viewmodel.MVVMViewModel
import java.lang.Math.random

class ExampleViewModel : MVVMViewModel() {

    var counter by bundleProperty(1)


    var counter2 by sharedProperty("counter", 1)

    val elements = ObservableList<String>()

    fun increase() {
        counter++
        elements += random().toString()
    }

    fun decrease() {
        counter--
        if (elements.isNotEmpty())
            elements -= elements.random()
    }
}
package com.whenwhat.fragments

import com.whenwhat.framework.property.property
import com.whenwhat.framework.viewmodel.MVVMViewModel

class FirstViewModel : MVVMViewModel() {

    var text by property("Hello world")
    var text2 by property("Hello world")
}
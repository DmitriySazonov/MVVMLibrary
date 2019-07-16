package com.whenwhat.mvvmlibrary.example

import com.whenwhat.mvvmlibrary.property.PropertyDelegate
import com.whenwhat.mvvmlibrary.viewmodel.MVVMViewModel

class SecondViewModel : MVVMViewModel() {
    var text by PropertyDelegate("")
}
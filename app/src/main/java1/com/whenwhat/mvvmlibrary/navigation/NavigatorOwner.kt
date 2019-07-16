package com.whenwhat.mvvmlibrary.navigation

import com.whenwhat.mvvmlibrary.view.MVVMActivity

interface NavigatorOwner {
    val name: String
    val screenCode: Int
    val prevScreenCode: Int?
    val mvvmActivity: MVVMActivity<*>
    val screenMetaData: ScreenMetaData
}
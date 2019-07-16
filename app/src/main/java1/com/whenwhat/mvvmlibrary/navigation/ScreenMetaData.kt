package com.whenwhat.mvvmlibrary.navigation

interface ScreenMetaData {
    val name: String
    val screenCode: Int
    val enterParams: String?
    val prevScreenCode: Int?
}

data class ActivityScreenMetaData(
        override val name: String,
        override val screenCode: Int,
        override val enterParams: String?,
        override val prevScreenCode: Int?
) : ScreenMetaData

data class FragmentScreenMetaData(
        val hostedOnActivity: ActivityScreenMetaData,
        override val name: String,
        override val screenCode: Int,
        override val enterParams: String?,
        override val prevScreenCode: Int?
) : ScreenMetaData
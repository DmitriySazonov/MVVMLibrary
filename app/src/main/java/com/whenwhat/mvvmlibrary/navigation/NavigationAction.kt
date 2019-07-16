package com.whenwhat.mvvmlibrary.navigation

import com.whenwhat.mvvmlibrary.navigation.point.PointMetaData
import java.io.Serializable

class NavigationAction(
    val navigationPointId: Long,
    val navigationPointData: Serializable
) : Serializable
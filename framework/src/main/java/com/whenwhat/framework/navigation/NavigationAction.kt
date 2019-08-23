package com.whenwhat.framework.navigation

import java.io.Serializable

data class NavigationAction(
    val pointName: String,
    val param: Serializable?
) : Serializable {
    companion object {
        const val BUNDLE_KEY = "NAVIGATION_ACTION"
    }
}
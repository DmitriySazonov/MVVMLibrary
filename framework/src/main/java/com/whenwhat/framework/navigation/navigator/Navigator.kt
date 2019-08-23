package com.whenwhat.framework.navigation.navigator

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.whenwhat.framework.navigation.NavigationAction
import com.whenwhat.framework.navigation.point.Empty
import com.whenwhat.framework.navigation.point.NavigationPoint
import java.io.Serializable


interface Navigator {

    companion object {
        private val returnedParams = HashMap<String, Serializable>()
    }

    val activity: Activity
    val pointName: String
    val prevPointName: String?
    fun openActivity(activityClass: Class<out Activity>, bundle: Bundle)
    fun navigate(navigationAction: NavigationAction)
    fun <IN : Serializable> navigate(navigationPoint: NavigationPoint<IN>, input: IN)
    fun navigate(navigationPoint: NavigationPoint<Empty>)

    fun back()

    fun backWith(param: Serializable) {
        if (prevPointName != null)
            returnedParams[prevPointName!!] = param
        back()
    }

    fun popResult(): Serializable? {
        return returnedParams.remove(pointName)
    }
}

interface FragmentNavigator : Navigator {
    val isRoot: Boolean
    fun openFragment(fragment: Fragment, tag: String)
    fun finishActivity()
}
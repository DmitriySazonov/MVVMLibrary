package com.whenwhat.framework.navigation.point

import android.app.Activity
import androidx.fragment.app.Fragment
import com.whenwhat.framework.navigation.NavigationAction
import com.whenwhat.framework.navigation.navigator.Navigator
import java.io.Serializable

abstract class HostActivityNavigationPoint<IN : Serializable>(
    name: String,
    val activityClass: Class<out Activity>
) : NavigationPoint<IN>(name) {

    private var rootNavigationPoint: String? = null

    override fun navigate(navigator: Navigator, input: IN) {
        if (rootNavigationPoint != null) {
            navigate(navigator, rootNavigationPoint!!, input)
        } else {
            navigator.openActivity(activityClass, bundle(navigator))
        }
    }

    fun navigate(navigator: Navigator, pointName: String, param: Serializable) {
        navigator.openActivity(activityClass, bundle(navigator).apply {
            putSerializable(
                NavigationAction.BUNDLE_KEY,
                NavigationAction(pointName, param)
            )
        })
    }

    protected fun <IN : Serializable> fragmentPoint(name: String, fragmentClass: Class<out Fragment>): NavigationPoint<IN> {
        return FragmentNavigationPoint(name, fragmentClass, this)
    }

    protected fun rootFragmentPoint(name: String, fragmentClass: Class<out Fragment>): NavigationPoint<IN> {
        rootNavigationPoint = name
        return FragmentNavigationPoint(name, fragmentClass, this)
    }
}

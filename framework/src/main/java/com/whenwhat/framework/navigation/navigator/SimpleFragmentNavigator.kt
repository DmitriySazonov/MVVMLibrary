package com.whenwhat.framework.navigation.navigator

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.whenwhat.framework.navigation.NavigationAction
import com.whenwhat.framework.navigation.point.Empty
import com.whenwhat.framework.navigation.point.NavigationPoint
import java.io.Serializable

class SimpleFragmentNavigator(val activityHostNavigator: HostActivityNavigator, val fragment: Fragment) :
    FragmentNavigator {

    override val pointName: String = obtainPointName()
    override val prevPointName: String? = obtainPrevPointName()
    override val isRoot: Boolean = false

    override val activity: Activity
        get() = activityHostNavigator.activity

    override fun openActivity(activityClass: Class<out Activity>, bundle: Bundle) {
        activityHostNavigator.openActivity(activityClass, bundle)
    }

    override fun back() {
        activityHostNavigator.back()
    }

    override fun openFragment(fragment: Fragment, tag: String) {
        activityHostNavigator.openFragment(fragment, tag)
    }

    override fun finishActivity() {
        activityHostNavigator.finishActivity()
    }

    @Suppress("UNCHECKED_CAST")
    override fun navigate(navigationAction: NavigationAction) {
        NavigationPoint.findByName(navigationAction.pointName)?.let {
            it as NavigationPoint<Serializable>
        }?.also {
            navigate(it, navigationAction.param ?: Empty)
        }
    }

    override fun <IN : Serializable> navigate(navigationPoint: NavigationPoint<IN>, input: IN) {
        navigationPoint.navigate(this, input)
    }

    override fun navigate(navigationPoint: NavigationPoint<Empty>) {
        navigate(navigationPoint, Empty)
    }

    private fun obtainPointName(): String {
        return fragment.arguments?.let { NavigationPoint.obtainPointName(it) } ?: fragment.javaClass.simpleName
    }

    private fun obtainPrevPointName(): String? {
        return fragment.arguments?.let { NavigationPoint.obtainPrevPointName(it) }
    }
}
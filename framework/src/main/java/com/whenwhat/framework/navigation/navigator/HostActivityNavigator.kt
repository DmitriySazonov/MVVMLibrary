package com.whenwhat.framework.navigation.navigator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.whenwhat.framework.navigation.NavigationAction
import com.whenwhat.framework.navigation.point.Empty
import com.whenwhat.framework.navigation.point.NavigationPoint
import java.io.Serializable

class HostActivityNavigator(override val activity: AppCompatActivity, @IdRes val containerId: Int) :
    FragmentNavigator {

    override val pointName: String = obtainPointName()
    override val prevPointName: String? = obtainPrevPointName()
    override val isRoot: Boolean = true

    override fun openActivity(activityClass: Class<out Activity>, bundle: Bundle) {
        activity.startActivity(Intent(activity, activityClass).putExtras(bundle))
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

    override fun openFragment(fragment: Fragment, tag: String) {
        activity.supportFragmentManager.run {
            beginTransaction().apply {
                if (fragments.isNotEmpty())
                    addToBackStack(tag)
                replace(containerId, fragment)
            }.commit()
        }
    }

    override fun back() {
        activity.onBackPressed()
    }

    override fun finishActivity() {
        activity.finish()
    }

    private fun obtainPointName(): String {
        return activity.intent?.extras?.let { NavigationPoint.obtainPointName(it) } ?: activity.javaClass.simpleName
    }

    private fun obtainPrevPointName(): String? {
        return activity.intent?.extras?.let { NavigationPoint.obtainPrevPointName(it) }
    }
}
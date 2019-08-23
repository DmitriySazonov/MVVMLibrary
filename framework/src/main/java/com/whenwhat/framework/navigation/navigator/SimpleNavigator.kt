package com.whenwhat.framework.navigation.navigator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.whenwhat.framework.navigation.NavigationAction
import com.whenwhat.framework.navigation.point.Empty
import com.whenwhat.framework.navigation.point.NavigationPoint
import java.io.Serializable

open class SimpleNavigator(override val activity: Activity) : Navigator {

    override val pointName: String = obtainPointName()
    override val prevPointName: String? = obtainPrevPointName()

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

    override fun back() {
        activity.finish()
    }

    private fun obtainPointName(): String {
        return activity.intent?.extras?.let { NavigationPoint.obtainPointName(it) } ?: activity.javaClass.simpleName
    }

    private fun obtainPrevPointName(): String? {
        return activity.intent?.extras?.let { NavigationPoint.obtainPrevPointName(it) }
    }
}
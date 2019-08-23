package com.whenwhat.framework.navigation.point

import android.app.Activity
import com.whenwhat.framework.navigation.navigator.Navigator
import java.io.Serializable

open class ActivityNavigationPoint<IN : Serializable>(
    name: String,
    val activityClass: Class<out Activity>
) : NavigationPoint<IN>(name) {

    override fun navigate(navigator: Navigator, input: IN) {
        navigator.openActivity(activityClass, wrapToBundle(navigator, input))
    }
}
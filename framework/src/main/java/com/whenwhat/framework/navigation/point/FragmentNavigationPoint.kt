package com.whenwhat.framework.navigation.point

import androidx.fragment.app.Fragment
import com.whenwhat.framework.navigation.navigator.FragmentNavigator
import com.whenwhat.framework.navigation.navigator.Navigator
import java.io.Serializable

class FragmentNavigationPoint<IN : Serializable>(
    name: String,
    val fragmentClass: Class<out Fragment>,
    private val hostActivityNavigationPoint: HostActivityNavigationPoint<*>
) : NavigationPoint<IN>(name) {

    override fun navigate(navigator: Navigator, input: IN) {
        if (navigator is FragmentNavigator && navigator.isSameActivity())
            navigator.openFragment(createFragment(navigator, input), name)
        else
            hostActivityNavigationPoint.navigate(navigator, name, input)
    }

    private fun createFragment(navigator: FragmentNavigator, input: IN): Fragment {
        return fragmentClass.newInstance().apply {
            arguments = wrapToBundle(navigator, input).apply {
                putString(PREV_POINT_NAME_BUNDLE_KEY, navigator.run { if (isRoot) prevPointName else pointName })
            }
        }
    }

    private fun FragmentNavigator.isSameActivity(): Boolean {
        return activity.javaClass.name == hostActivityNavigationPoint.activityClass.name
    }
}
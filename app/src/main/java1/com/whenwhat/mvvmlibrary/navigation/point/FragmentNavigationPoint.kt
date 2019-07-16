package com.whenwhat.mvvmlibrary.navigation.point

import android.content.Intent
import android.os.Bundle
import com.whenwhat.mvvmlibrary.navigation.NavigationAction
import com.whenwhat.mvvmlibrary.navigation.NavigationFragmentNode
import com.whenwhat.mvvmlibrary.navigation.NavigationParam
import com.whenwhat.mvvmlibrary.view.MVVMActivity
import com.whenwhat.mvvmlibrary.view.MVVMFragment
import com.whenwhat.mvvmlibrary.view.MVVMHostActivity
import java.io.Serializable

/**
 * @param F тип фрагмента на которого происходит навигация
 * @param IN Тип параметра передающийся на экран
 * @param OUT Тип параметра возвращаемый экраном
 * */
class FragmentNavigationPoint<F : MVVMFragment<*>, IN : Serializable, OUT : Serializable>(
    override val parent: NavigationFragmentNode<*>,
    private val fragmentClass: Class<F>
) : NavigationPoint<IN, OUT>() {

    override fun navigate(activity: MVVMActivity<*>, param: IN, pointMetaData: PointMetaData) {
        if (parent.activityClass != activity.javaClass) {
            navigateToHostActivity(activity, param, pointMetaData)
        } else if (activity is MVVMHostActivity<*>) {
            navigateToFragment(activity, param, pointMetaData)
        }
    }

    private fun navigateToHostActivity(activity: MVVMActivity<*>, param: IN, metaData: PointMetaData) {
        val navigationAction = NavigationAction(identifier, param, metaData)
        Intent(activity, parent.activityClass).apply {
            putExtra(NavigationParam.BUNDLE_KEY, NavigationParam(navigationAction))
        }.also(activity::startActivity)
    }

    private fun navigateToFragment(activity: MVVMHostActivity<*>, param: IN, metaData: PointMetaData) {
        val fragment = fragmentClass.newInstance()
        fragment.arguments = Bundle().apply {
            putSerializable(NavigationParam.BUNDLE_KEY, NavigationParam(param, metaData))
        }
        activity.navigateTo(fragment)
    }
}
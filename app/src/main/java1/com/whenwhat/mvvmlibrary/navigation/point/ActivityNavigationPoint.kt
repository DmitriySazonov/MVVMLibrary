package com.whenwhat.mvvmlibrary.navigation.point

import android.content.Intent
import com.whenwhat.mvvmlibrary.navigation.NavigationNode
import com.whenwhat.mvvmlibrary.navigation.NavigationParam
import com.whenwhat.mvvmlibrary.view.MVVMActivity
import java.io.Serializable

/**
 * @param A тип активити на которую происходит навигация
 * @param IN Тип параметра передающийся на экран
 * @param OUT Тип параметра возвращаемый экраном
 * */
class ActivityNavigationPoint<A : MVVMActivity<*>, IN : Serializable, OUT : Serializable>(
    override val parent: NavigationNode,
    private val activityClass: Class<A>
) : NavigationPoint<IN, OUT>() {

    override fun navigate(activity: MVVMActivity<*>, param: IN, pointMetaData: PointMetaData) {
        Intent(activity, activityClass).apply {
            putExtra(NavigationParam.BUNDLE_KEY, NavigationParam(param, pointMetaData))
        }.also(activity::startActivity)
    }
}
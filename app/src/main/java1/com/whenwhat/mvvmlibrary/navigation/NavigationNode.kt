package com.whenwhat.mvvmlibrary.navigation

import com.whenwhat.mvvmlibrary.navigation.point.ActivityNavigationPoint
import com.whenwhat.mvvmlibrary.navigation.point.NavigationPoint
import com.whenwhat.mvvmlibrary.view.MVVMActivity
import java.io.Serializable
import java.util.*

abstract class NavigationNode {

    val nestedPoints: List<NavigationPoint<*, *>>
        get() = mNestedPoints

    private val mNestedPoints = LinkedList<NavigationPoint<*, *>>()

    /**
     * @param A mvvmActivity class
     * @param IN Тип параметра передающийся на экран
     * @param OUT Тип параметра возвращаемый экраном
     * */
    protected fun <A : MVVMActivity<*>, IN : Serializable, OUT : Serializable> navigationActivity(activityClass: Class<A>): NavigationPoint<IN, OUT> {
        return ActivityNavigationPoint<A, IN, OUT>(this, activityClass).also {
            mNestedPoints += it
        }
    }
}
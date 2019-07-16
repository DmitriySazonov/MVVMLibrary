package com.whenwhat.mvvmlibrary.navigation

import com.whenwhat.mvvmlibrary.navigation.point.FragmentNavigationPoint
import com.whenwhat.mvvmlibrary.navigation.point.NavigationPoint
import com.whenwhat.mvvmlibrary.view.MVVMFragment
import com.whenwhat.mvvmlibrary.view.MVVMHostActivity
import java.io.Serializable


abstract class NavigationFragmentNode<A : MVVMHostActivity<*>>(val activityClass: Class<A>) : NavigationNode() {

    /**
     * @param F тип фрагмента на которого происходит навигация
     * @param IN Тип параметра передающийся на экран
     * @param OUT Тип параметра возвращаемый экраном
     * */
    protected fun <F : MVVMFragment<*>, IN : Serializable, OUT : Serializable> navigationFragment(fragmentClass: Class<F>): NavigationPoint<IN, OUT> {
        return FragmentNavigationPoint(this, fragmentClass)
    }
}
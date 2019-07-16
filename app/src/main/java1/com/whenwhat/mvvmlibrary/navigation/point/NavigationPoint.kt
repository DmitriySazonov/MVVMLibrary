package com.whenwhat.mvvmlibrary.navigation.point

import android.annotation.SuppressLint
import com.whenwhat.mvvmlibrary.navigation.NavigationNode
import com.whenwhat.mvvmlibrary.other.longIncrement
import com.whenwhat.mvvmlibrary.view.MVVMActivity
import java.io.Serializable

/**
 * @param IN Тип параметра передающийся на экран
 * @param OUT Тип параметра возвращаемый экраном
 * */
@SuppressLint("UseSparseArrays")
abstract class NavigationPoint<IN : Serializable, OUT : Serializable> {

    companion object {
        private val increment by longIncrement(0)
        val navigationPoints: Map<Long, NavigationPoint<*, *>>
            get() = sNavigationPoints
        private val sNavigationPoints = HashMap<Long, NavigationPoint<*, *>>()
    }

    val identifier: Long = increment

    init {
        sNavigationPoints[identifier] = this
    }

    abstract val parent: NavigationNode
    abstract fun navigate(activity: MVVMActivity<*>, param: IN, pointMetaData: PointMetaData)
}
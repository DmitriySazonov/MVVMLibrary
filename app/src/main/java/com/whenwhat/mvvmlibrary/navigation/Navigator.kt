package com.whenwhat.mvvmlibrary.navigation

import android.annotation.SuppressLint
import android.util.Log
import com.whenwhat.mvvmlibrary.BuildConfig
import com.whenwhat.mvvmlibrary.navigation.point.NavigationPoint
import com.whenwhat.mvvmlibrary.navigation.point.PointMetaData
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

interface Navigator {

    companion object {
        @SuppressLint("UseSparseArrays")
        private val resultStorage = HashMap<Int, Serializable>()
        private val screenStack = LinkedList<ScreenMetaData>()

        fun screenStarted(screenMetaData: ScreenMetaData) {
            if (!BuildConfig.DEBUG)
                return
            screenStack.add(screenMetaData)
            printNavigationStack()
        }

        fun screenFinished(screenMetaData: ScreenMetaData) {
            if (!BuildConfig.DEBUG)
                return
            screenStack.removeAll {
                it.screenCode == screenMetaData.screenCode
            }
            printNavigationStack()
        }

        fun storeResult(code: Int, data: Serializable) {
            resultStorage[code] = data
        }

        fun popResult(code: Int): Serializable? {
            return resultStorage.remove(code)
        }

        fun printNavigationStack() {
            screenStack.joinToString(separator = "\n====================\n").also {
                Log.d("NavigationStack", "ScreenStack: \n$it")
            }
        }
    }

    fun navigate(navigationAction: NavigationAction, metaData: PointMetaData? = null)
    /**
     * @return resultCode
     * */
    fun <IN : Serializable, OUT : Serializable> navigate(
        navigationPoint: NavigationPoint<IN, OUT>,
        param: IN,
        metaData: PointMetaData? = null
    )

    fun navigateBack()
    fun navigateBackWith(data: Serializable)

    fun obtainResult(): Serializable?

    fun onCreate()
    fun onDestroy()
}

class DefaultNavigator(private val owner: NavigatorOwner) : Navigator {

    @Suppress("UNCHECKED_CAST")
    override fun navigate(navigationAction: NavigationAction, metaData: PointMetaData?) {
        NavigationPoint.navigationPoints[navigationAction.navigationPointId]?.also {
            navigate(
                navigationPoint = it as NavigationPoint<Serializable, Serializable>,
                param = navigationAction.navigationPointData,
                metaData = metaData
            )
        }
    }

    override fun onCreate() {
        Navigator.screenStarted(owner.screenMetaData)
    }

    override fun onDestroy() {
        Navigator.screenFinished(owner.screenMetaData)
    }

    override fun <IN : Serializable, OUT : Serializable> navigate(
        navigationPoint: NavigationPoint<IN, OUT>,
        param: IN,
        metaData: PointMetaData?
    ) {
        navigationPoint.navigate(
            activity = owner.mvvmActivity,
            param = param,
            pointMetaData = metaData ?: PointMetaData(owner.screenCode)
        )
    }

    override fun navigateBack() = navigateToBack()

    override fun navigateBackWith(data: Serializable) {
        val prevScreenCode = owner.prevScreenCode ?: throw IllegalArgumentException(
            "The previous screen does not exist"
        )
        Navigator.storeResult(prevScreenCode, data)
        navigateBack()
    }

    override fun obtainResult(): Serializable? {
        return Navigator.popResult(owner.screenCode)
    }

    private fun navigateToBack() {
        owner.mvvmActivity.onBackPressed()
    }
}
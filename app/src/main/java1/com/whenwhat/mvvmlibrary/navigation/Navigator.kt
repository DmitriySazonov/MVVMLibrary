package com.whenwhat.mvvmlibrary.navigation

import android.annotation.SuppressLint
import android.util.Log
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
            screenStack.add(screenMetaData)
        }

        fun screenFinished(screenMetaData: ScreenMetaData) {
            screenStack.removeAll {
                it.screenCode == screenMetaData.screenCode
            }
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

    fun navigate(navigationAction: NavigationAction)
    /**
     * @return resultCode
     * */
    fun <IN : Serializable, OUT : Serializable> navigate(navigationPoint: NavigationPoint<IN, OUT>, param: IN)

    fun navigateBack()
    fun navigateBackWith(data: Serializable)

    fun obtainResult(): Serializable?
}

class DefaultNavigator(private val owner: NavigatorOwner) : Navigator {

    @Suppress("UNCHECKED_CAST")
    override fun navigate(navigationAction: NavigationAction) {
        NavigationPoint.navigationPoints[navigationAction.navigationPointId]?.also {
            navigate(it as NavigationPoint<Serializable, Serializable>,
                    navigationAction.navigationPointData, navigationAction.pointMetaData)
        }
    }

    override fun <IN : Serializable, OUT : Serializable> navigate(
            navigationPoint: NavigationPoint<IN, OUT>,
            param: IN
    ) {
        navigate(navigationPoint, param, PointMetaData(owner.screenCode))
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
        Navigator.screenFinished(owner.screenMetaData)
        owner.mvvmActivity.onBackPressed()
    }

    private fun <IN : Serializable> navigate(navigationPoint: NavigationPoint<IN, *>, param: IN, metaData: PointMetaData) {
        navigationPoint.navigate(owner.mvvmActivity, param, metaData)
        Navigator.screenStarted(owner.screenMetaData)
    }
}
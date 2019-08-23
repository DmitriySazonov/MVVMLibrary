package com.whenwhat.framework.navigation.point

import android.os.Bundle
import com.whenwhat.framework.navigation.navigator.Navigator
import java.io.Serializable
import java.lang.ref.WeakReference

object Empty : Serializable

fun NavigationPoint<Empty>.navigate(navigator: Navigator) {
    navigate(navigator, Empty)
}

abstract class NavigationPoint<IN : Serializable>(val name: String) {

    companion object {
        const val PREV_POINT_NAME_BUNDLE_KEY = "PREV_POINT_NAME_BUNDLE_KEY"
        const val POINT_NAME_BUNDLE_KEY = "POINT_NAME_BUNDLE_KEY"

        private val sNavigationPoints = HashMap<String, WeakReference<NavigationPoint<*>>>()

        fun findByName(name: String): NavigationPoint<*>? {
            return sNavigationPoints[name]?.get()
        }

        fun obtainPointName(bundle: Bundle): String? {
            return bundle.getString(POINT_NAME_BUNDLE_KEY)
        }

        fun obtainPrevPointName(bundle: Bundle): String? {
            return bundle.getString(PREV_POINT_NAME_BUNDLE_KEY)
        }
    }

    init {
        sNavigationPoints.put(name, WeakReference(this))
    }

    abstract fun navigate(navigator: Navigator, input: IN)

    protected fun bundle(navigator: Navigator): Bundle {
        return Bundle().apply {
            putString(PREV_POINT_NAME_BUNDLE_KEY, navigator.pointName)
            putString(POINT_NAME_BUNDLE_KEY, name)
        }
    }

    protected fun wrapToBundle(navigator: Navigator, vararg params: Serializable): Bundle {
        return bundle(navigator).apply {
            params.forEach {
                putSerializable(it.key(), it)
            }
        }
    }

    protected fun Serializable.key(): String {
        return "${name}_${javaClass.simpleName}"
    }
}
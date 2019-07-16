package com.whenwhat.mvvmlibrary.view

import android.os.Bundle
import com.whenwhat.mvvmlibrary.navigation.NavigationAction
import com.whenwhat.mvvmlibrary.viewmodel.MVVMHostViewModel

abstract class MVVMHostActivity<VM : MVVMHostViewModel> : MVVMActivity<VM>() {

    companion object {
        private const val WAS_ROOT_NAVIGATE = "WAS_ROOT_NAVIGATE"
    }

    abstract fun getContainerId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState?.getBoolean(WAS_ROOT_NAVIGATE, false) != true)
            getPrevScreenParam<NavigationAction>().also(::navigate)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(WAS_ROOT_NAVIGATE, true)
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.takeIf { fragments ->
            fragments.any {
                (it as? MVVMFragment<*>)?.onBackPressed() == true
            }
        } ?: super.onBackPressed()
    }

    fun navigateTo(fragment: MVVMFragment<*>) {
        supportFragmentManager.beginTransaction().apply {
            if (supportFragmentManager.fragments.isNotEmpty())
                addToBackStack(fragment.tag)
            replace(getContainerId(), fragment)
        }.commit()
    }

    private fun navigate(navigationAction: NavigationAction?) {
        if (navigationAction != null) {
            navigator.navigate(navigationAction, pointMetaData)
        } else {
            throw IllegalArgumentException("Cannot be open MVVMHostActivity without NavigationAction")
        }
    }
}
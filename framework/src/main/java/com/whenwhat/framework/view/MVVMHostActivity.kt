package com.whenwhat.framework.view

import android.os.Bundle
import com.whenwhat.framework.navigation.NavigationAction
import com.whenwhat.framework.navigation.navigator.HostActivityNavigator
import com.whenwhat.framework.navigation.navigator.Navigator
import com.whenwhat.framework.viewmodel.MVVMHostViewModel

abstract class MVVMHostActivity<VM : MVVMHostViewModel> : MVVMActivity<VM>() {

    companion object {
        private const val WAS_ROOT_NAVIGATE = "WAS_ROOT_NAVIGATE"
    }

    override val navigator: HostActivityNavigator?
        get() = super.navigator as? HostActivityNavigator

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

    override fun createNavigator(): Navigator {
        return HostActivityNavigator(this, getContainerId())
    }

    private fun navigate(navigationAction: NavigationAction?) {
        if (navigationAction != null) {
            navigator?.navigate(navigationAction)
        } else {
            throw IllegalArgumentException("Cannot be open MVVMHostActivity without NavigationAction")
        }
    }
}
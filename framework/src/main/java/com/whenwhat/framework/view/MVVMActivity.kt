package com.whenwhat.framework.view

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.whenwhat.framework.navigation.navigator.Navigator
import com.whenwhat.framework.navigation.navigator.SimpleNavigator
import com.whenwhat.framework.other.Subscription
import com.whenwhat.framework.other.getObject
import com.whenwhat.framework.viewmodel.MVVMViewModel
import java.io.Serializable
import java.util.*


abstract class MVVMActivity<VM : MVVMViewModel> : AppCompatActivity(), MVVMViewDelegate<VM>, MVVMView<VM> by DefaultMVVMView() {

    private val subscriptions = LinkedList<Subscription>()

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreate(this, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        navigator?.popResult()?.also(::onForwardScreenResult)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        bindViewModel(viewModel ?: return)
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        unBindViewModel(viewModel ?: return)
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        onSaveInstanceState(this, outState)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        onDestroy(this, isChangingConfigurations)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun createNavigator(): Navigator {
        return SimpleNavigator(this)
    }

    open fun onForwardScreenResult(result: Serializable) {

    }

    @CallSuper
    open fun bindViewModel(viewModel: VM) {

    }

    @CallSuper
    open fun unBindViewModel(viewModel: VM) {
        subscriptions.forEach { it.unSubscribe() }
    }

    protected inline fun <reified T> getPrevScreenParam(): T? {
        return intent.extras?.getObject<T>()
    }

    /**
     * shortcut для autoRelease
     * */
    protected operator fun Subscription?.unaryMinus() {
        autoRelease()
    }

    /**
     * Подписки находящие в списке subscriptions отчистятся сами при уничтожении View
     * */
    protected fun Subscription?.autoRelease() {
        if (this != null)
            subscriptions += this
    }
}

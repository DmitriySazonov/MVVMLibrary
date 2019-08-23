package com.whenwhat.framework.view

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.whenwhat.framework.navigation.navigator.Navigator
import com.whenwhat.framework.navigation.navigator.SimpleFragmentNavigator
import com.whenwhat.framework.other.Subscription
import com.whenwhat.framework.other.getObject
import com.whenwhat.framework.viewmodel.MVVMViewModel
import java.io.Serializable
import java.util.*

abstract class MVVMFragment<VM : MVVMViewModel> : Fragment(), MVVMViewDelegate<VM>, MVVMView<VM> by DefaultMVVMView() {

    private val subscriptions = LinkedList<Subscription>()

    @CallSuper
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /**
         * Если isCreated == true значит экземпляр фрагмента не был уничтожен и
         * его не нужно заново инициализировать
         * */
        if (!isCreated)
            onCreate(this, savedInstanceState)
        bindViewModel(viewModel ?: return)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        navigator?.popResult()?.also(::onForwardScreenResult)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        unBindViewModel(viewModel ?: return)
    }

    @CallSuper
    override fun onDetach() {
        super.onDetach()
        val isChangingConfigurations = activity?.isChangingConfigurations == true
        onDestroy(this, isChangingConfigurations)
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        onSaveInstanceState(this, outState)
    }

    override fun createNavigator(): Navigator {
        val activity = activity as? MVVMHostActivity<*>
            ?: throw IllegalArgumentException("activity must be extends MVVMHostActivity")
        val navigator = activity.navigator!!
        return SimpleFragmentNavigator(navigator, this)
    }

    open fun onForwardScreenResult(result: Serializable) {

    }

    /**
     * @return true если действие переопределено, false если нужно выполнить
     * стандартное действие
     * */
    open fun onBackPressed(): Boolean {
        return false
    }

    @CallSuper
    open fun bindViewModel(viewModel: VM) {

    }

    @CallSuper
    open fun unBindViewModel(viewModel: VM) {
        subscriptions.forEach { it.unSubscribe() }
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

    inline fun <reified T> getPrevScreenParam(): T? {
        return arguments?.getObject<T>()
    }
}
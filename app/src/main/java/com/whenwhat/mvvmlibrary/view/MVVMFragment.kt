package com.whenwhat.mvvmlibrary.view

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.whenwhat.mvvmlibrary.navigation.*
import com.whenwhat.mvvmlibrary.navigation.point.PointMetaData
import com.whenwhat.mvvmlibrary.other.Subscription
import com.whenwhat.mvvmlibrary.other.getObject
import com.whenwhat.mvvmlibrary.viewmodel.MVVMViewModel
import java.io.Serializable
import java.util.*

abstract class MVVMFragment<VM : MVVMViewModel> : Fragment(), MVVMViewOwner<VM>, NavigatorOwner {

    override val name: String = javaClass.simpleName
    override val screenCode: Int
        get() = mvvmView.screenCode
    override val prevScreenCode: Int?
        get() = getPrevScreenParam<PointMetaData>()?.prevScreenCode
    override val mvvmActivity: MVVMActivity<*>
        get() = activity as MVVMActivity<*>
    override val screenMetaData by lazy(::createScreenMetaData)

    val navigator: Navigator by lazy { DefaultNavigator(this) }
    val viewModel: VM get() = mvvmView.viewModel
    private val mvvmView = MVVMView(this)
    private val subscriptions = LinkedList<Subscription>()

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mvvmView.onCreate(savedInstanceState)
        if (savedInstanceState == null)
            navigator.onCreate()
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewMode(viewModel)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        unBindViewMode(viewModel)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        navigator.obtainResult()?.also(::onForwardScreenResult)
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvvmView.onSaveInstanceState(outState)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        val isChangingConfigurations = activity?.isChangingConfigurations == true
        if (!isChangingConfigurations)
            navigator.onDestroy()
        mvvmView.onDestroy(isChangingConfigurations)
    }

    /**
     * @return true если действие переопределено, false если нужно выполнить
     * стандартное действие
     * */
    open fun onBackPressed(): Boolean {
        return false
    }

    open fun onForwardScreenResult(data: Serializable) {

    }

    @CallSuper
    open fun bindViewMode(viewModel: VM) {

    }

    @CallSuper
    open fun unBindViewMode(viewModel: VM) {
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
        return arguments?.getObject<NavigationParam>()?.getObject()
    }

    private fun createScreenMetaData(): FragmentScreenMetaData {
        return FragmentScreenMetaData(
            hostedOnActivity = mvvmActivity.screenMetaData,
            name = javaClass.name,
            screenCode = screenCode,
            enterParams = arguments?.getObject<NavigationParam>()?.toString(),
            prevScreenCode = prevScreenCode
        )
    }
}
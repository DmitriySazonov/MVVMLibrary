package com.whenwhat.mvvmlibrary.view

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.whenwhat.mvvmlibrary.navigation.*
import com.whenwhat.mvvmlibrary.navigation.point.PointMetaData
import com.whenwhat.mvvmlibrary.other.Subscription
import com.whenwhat.mvvmlibrary.other.getObject
import com.whenwhat.mvvmlibrary.viewmodel.MVVMViewModel
import java.io.Serializable
import java.util.*

abstract class MVVMActivity<VM : MVVMViewModel> : AppCompatActivity(), MVVMViewOwner<VM>, NavigatorOwner {

    override val name: String = javaClass.simpleName
    override val screenCode: Int
        get() = mvvmView.screenCode
    override val prevScreenCode: Int?
        get() = pointMetaData?.prevScreenCode
    override val mvvmActivity: MVVMActivity<*>
        get() = this
    override val screenMetaData by lazy(::createScreenMetaData)

    val pointMetaData by lazy { getPrevScreenParam<PointMetaData>() }
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
    override fun onStart() {
        super.onStart()
        bindViewMode(viewModel)
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
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
        if (!isChangingConfigurations)
            navigator.onDestroy()
        mvvmView.onDestroy(isChangingConfigurations)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
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
        return intent.extras?.getObject<NavigationParam>()?.getObject()
    }

    private fun createScreenMetaData(): ActivityScreenMetaData {
        return ActivityScreenMetaData(
            name = javaClass.name,
            screenCode = screenCode,
            enterParams = intent.extras?.getObject<NavigationParam>()?.toString(),
            prevScreenCode = prevScreenCode
        )
    }
}
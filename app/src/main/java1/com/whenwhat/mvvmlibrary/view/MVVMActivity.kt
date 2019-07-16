package com.whenwhat.mvvmlibrary.view

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import com.whenwhat.mvvmlibrary.navigation.*
import com.whenwhat.mvvmlibrary.navigation.point.PointMetaData
import com.whenwhat.mvvmlibrary.other.getObject
import com.whenwhat.mvvmlibrary.viewmodel.MVVMViewModel
import java.io.Serializable

abstract class MVVMActivity<VM : MVVMViewModel> : AppCompatActivity(), MVVMViewOwner<VM>, NavigatorOwner {

    override val name: String = javaClass.simpleName
    override val screenCode: Int
        get() = mvvmView.screenCode
    override val prevScreenCode: Int?
        get() = getPrevScreenParam<PointMetaData>()?.prevScreenCode
    override val mvvmActivity: MVVMActivity<*>
        get() = this
    override val screenMetaData by lazy(::createScreenMetaData)

    val navigator: Navigator by lazy { DefaultNavigator(this) }
    val viewModel: VM get() = mvvmView.viewModel
    private val mvvmView = MVVMView(this)

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mvvmView.onCreate(savedInstanceState)
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
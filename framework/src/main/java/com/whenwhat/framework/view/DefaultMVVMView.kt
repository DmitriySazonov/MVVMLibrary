package com.whenwhat.framework.view

import android.os.Bundle
import com.whenwhat.framework.navigation.navigator.Navigator
import com.whenwhat.framework.other.intIncrement
import com.whenwhat.framework.viewmodel.MVVMViewModel

class DefaultMVVMView<VM : MVVMViewModel> : MVVMView<VM> {

    companion object {
        private const val VIEW_SCREEN_CODE = "VIEW_SCREEN_CODE"
        private val screenCodeIncrement by intIncrement(1)
    }

    override var screenCode: Int = -1
        private set
    override var viewModel: VM? = null
        private set
    override var navigator: Navigator? = null
        private set
    override var isCreated: Boolean = false
        private set

    override fun onCreate(delegate: MVVMViewDelegate<VM>, savedInstanceState: Bundle?) {
        savedInstanceState?.getInt(VIEW_SCREEN_CODE, -1).also { code ->
            screenCode = code.takeIf { it != -1 } ?: screenCodeIncrement
        }
        navigator = delegate.createNavigator()
        viewModel = obtainViewModel(delegate)
        viewModel?.onCreate(savedInstanceState)
        isCreated = true
    }

    override fun onSaveInstanceState(delegate: MVVMViewDelegate<VM>, outState: Bundle) {
        outState.putInt(VIEW_SCREEN_CODE, screenCode)
        viewModel?.onSaveState(outState)
    }

    override fun onDestroy(delegate: MVVMViewDelegate<VM>, isChangingConfigurations: Boolean) {
        isCreated = false
        navigator = null
        /*
        *  Если идет пересоздания View то удалять ViewModel не нужно
        * */
        if (isChangingConfigurations)
            return
        /*
         * Удаляем ViewModel если она создана для конкретного экземпляра экрана
         * */
        if (delegate.getViewModelLifeType() == ViewModelLifeType.LOCAL) {
            ViewModelStorage.remove(delegate.getVMStoreKey())
            viewModel?.onDestroy()
        }
        viewModel = null
    }

    private fun obtainViewModel(delegate: MVVMViewDelegate<VM>): VM {
        return ViewModelStorage.obtain(delegate.getVMStoreKey()) ?: delegate.createViewModel().also {
            ViewModelStorage.store(delegate.getVMStoreKey(), it)
        }
    }

    private val MVVMViewDelegate<VM>.storeKeySuffix
        get() = when (val type = getViewModelLifeType()) {
            ViewModelLifeType.LOCAL -> screenCode.toString()
            ViewModelLifeType.GLOBAL -> type.name
        }

    private fun MVVMViewDelegate<VM>.getVMStoreKey(): String {
        return "${javaClass.name}-$storeKeySuffix-${getViewModelTag()}"
    }
}
package com.whenwhat.mvvmlibrary.view

import com.whenwhat.mvvmlibrary.viewmodel.MVVMViewModel

object ViewModelStorage {

    private val viewModelsMap = HashMap<String, MVVMViewModel>()

    fun store(view: MVVMView<*>, viewModel: MVVMViewModel) {
        viewModelsMap[view.viewModelStoreKey] = viewModel
    }

    fun remove(view: MVVMView<*>) {
        viewModelsMap.remove(view.viewModelStoreKey)
    }

    @Suppress("UNCHECKED_CAST")
    fun <VM : MVVMViewModel> obtain(view: MVVMView<VM>): VM? {
        return viewModelsMap[view.viewModelStoreKey] as? VM
    }
}
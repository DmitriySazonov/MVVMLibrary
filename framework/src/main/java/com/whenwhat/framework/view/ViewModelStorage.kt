package com.whenwhat.framework.view

import com.whenwhat.framework.viewmodel.MVVMViewModel

object ViewModelStorage {

    private val viewModelsMap = HashMap<String, MVVMViewModel>()

    fun store(key: String, viewModel: MVVMViewModel) {
        viewModelsMap[key] = viewModel
    }

    fun remove(key: String) {
        viewModelsMap.remove(key)
    }

    @Suppress("UNCHECKED_CAST")
    fun <VM : MVVMViewModel> obtain(key: String): VM? {
        return viewModelsMap[key] as? VM
    }
}
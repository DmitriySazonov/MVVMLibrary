package com.whenwhat.mvvmlibrary.view

import android.os.Bundle
import com.whenwhat.mvvmlibrary.other.intIncrement
import com.whenwhat.mvvmlibrary.viewmodel.MVVMViewModel

/**
 * Класс управляющий ViewModel-ю. Этот код общий для Activity и Fragment.
 * Все методы интерфеса MVVMViewLifeCycle должны быть вызваны из MVVMActivity или MVVMFragment
 * @see MVVMActivity
 * @see MVVMFragment
 * */
class MVVMView<VM : MVVMViewModel>(private val owner: MVVMViewOwner<VM>) : MVVMViewLifeCycle {

    companion object {
        private const val VIEW_MODEL_KEY = "VIEW_MODEL_KEY"
        private const val VIEW_MODEL_DATA = "VIEW_MODEL_DATA"
        private const val VIEW_SCREEN_CODE = "VIEW_MODEL_DATA"
        private val screenCodeIncrement by intIncrement(1)
    }

    val viewModel: VM by lazy(::obtainViewModel)
    var viewModelStoreKey = "${owner.javaClass.name}-${owner.storeKeySuffix}"
        private set
    var screenCode: Int = -1
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        savedInstanceState?.getString(VIEW_MODEL_KEY)?.also {
            viewModelStoreKey = it
        }
        savedInstanceState?.getInt(VIEW_SCREEN_CODE, -1).also { code ->
            screenCode = code.takeIf { it != -1 } ?: screenCodeIncrement
        }
        viewModel.onCreate(savedInstanceState?.getSerializable(VIEW_MODEL_DATA))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(VIEW_MODEL_KEY, viewModelStoreKey)
        outState.putInt(VIEW_SCREEN_CODE, screenCode)
        viewModel.onSaveState()?.also {
            outState.putSerializable(VIEW_MODEL_DATA, viewModel.onSaveState())
        }
    }

    override fun onDestroy(isChangingConfigurations: Boolean) {
        /*
        *  Если идет пересоздания View то удалять ViewModel не нужно
        * */
        if (isChangingConfigurations)
            return
        /*
         * Удаляем ViewModel если она создана для конкретного экземпляра экрана
         * */
        if (owner.getViewModelLifeType() == ViewModelLifeType.LOCAL)
            ViewModelStorage.remove(this)
        viewModel.onDestroy()
    }

    private fun obtainViewModel(): VM {
        return ViewModelStorage.obtain(this) ?: owner.createViewModel().also {
            ViewModelStorage.store(this, it)
        }
    }

    private val MVVMViewOwner<VM>.storeKeySuffix
        get() = when (val type = getViewModelLifeType()) {
            ViewModelLifeType.LOCAL -> owner.hashCode().toString()
            ViewModelLifeType.GLOBAL -> type.name
        }
}
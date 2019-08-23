package com.whenwhat.framework.viewmodel

import android.os.Bundle
import androidx.annotation.CallSuper
import com.whenwhat.framework.property.PropertyDelegate
import com.whenwhat.framework.property.bundleProperty
import java.io.Serializable

abstract class MVVMViewModel {

    companion object {
        private const val SAVED_INSTANCE_KEY = "VIEW_MODEL_DATA"
    }

    private val savedInstance: Bundle = Bundle()

    fun <T : Serializable> bundleProperty(initialValue: T, onChange: ((T) -> Unit)? = null): PropertyDelegate<T> {
        return bundleProperty(initialValue, savedInstance, onChange)
    }

    /**
     * Сохранние состояния ViewModel. Если система завершит процесс
     * приложения, то после востановления ViewModel эти данные попадут на вход
     * метода OnCreate
     * */
    @CallSuper
    open fun onSaveState(bundle: Bundle) {
        bundle.putBundle(SAVED_INSTANCE_KEY, savedInstance)
    }

    /**
     * Вызывается единажды при создании ViewModel
     * @param bundle сохраненное в методе onSaveState состояние.
     * */
    @CallSuper
    open fun onCreate(bundle: Bundle?) {
        if (bundle == null) onFirstCreate() else onReCreate(bundle)
    }

    /**
     * Вызывается единажды при первом создании ViewModel
     * */
    open fun onFirstCreate() {

    }

    /**
     * Вызывается единажды при пересоздании ViewModel
     * @param bundle сохраненное в методе onSaveState состояние.
     * */
    @CallSuper
    open fun onReCreate(bundle: Bundle) {
        bundle.getBundle(SAVED_INSTANCE_KEY)?.also(savedInstance::putAll)
    }

    /**
     * Вызывается единажды при уничтожении ViewModel
     * */
    open fun onDestroy() = Unit
}
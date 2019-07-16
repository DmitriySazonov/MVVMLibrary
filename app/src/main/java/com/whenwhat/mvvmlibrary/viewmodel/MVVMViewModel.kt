package com.whenwhat.mvvmlibrary.viewmodel

import androidx.annotation.CallSuper
import java.io.Serializable

abstract class MVVMViewModel {

    /**
     * Сохранние состояния ViewModel. Если система завершит процесс
     * приложения, то после постановления ViewModel эти данные попадут на вход
     * метода OnCreate
     * @return сохраняемый объект
     * */
    open fun onSaveState(): Serializable? = null

    /**
     * Вызывается единажды при создании ViewModel
     * @param state сохраненное в методе onSaveState состояние.
     * */
    @CallSuper
    open fun onCreate(state: Serializable?) {
        if (state == null) onFirstCreate() else onReCreate(state)
    }

    /**
     * Вызывается единажды при первом создании ViewModel
     * */
    open fun onFirstCreate() {

    }

    /**
     * Вызывается единажды при пересоздании ViewModel
     * @param state сохраненное в методе onSaveState состояние.
     * */
    open fun onReCreate(state: Serializable) {

    }

    /**
     * Вызывается единажды при уничтожении ViewModel
     * */
    open fun onDestroy() = Unit
}
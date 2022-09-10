package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    val errorData = MutableSharedFlow<Throwable>()

    protected open val errorHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
        viewModelScope.launch { errorData.emit(throwable) }
    }
}
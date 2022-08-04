package com.github.clockworkclyde.basedeliverymvvm.presentation.vm.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    val errorData = MutableSharedFlow<Throwable>()

    protected open val errorHandler = CoroutineExceptionHandler { _, e ->
        //Timber.e
        viewModelScope.launch { errorData.emit(e) }
    }
}
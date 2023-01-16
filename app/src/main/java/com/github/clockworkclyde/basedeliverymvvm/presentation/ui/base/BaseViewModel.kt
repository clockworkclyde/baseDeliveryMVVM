package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.util.loggError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    val errorData = MutableSharedFlow<Throwable>()

    protected open val errorHandler = CoroutineExceptionHandler { _, throwable ->
        loggError { throwable }
        viewModelScope.launch { errorData.emit(throwable) }
    }
}
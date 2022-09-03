package com.github.clockworkclyde.network.api

sealed class ViewState<out T> {
    object Loading : ViewState<Nothing>()
    object Empty : ViewState<Nothing>()
    data class Success<T>(val data: T) : ViewState<T>()
    data class Error(val throwable: Throwable) : ViewState<Nothing>()
}
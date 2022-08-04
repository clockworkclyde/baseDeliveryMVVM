package com.github.clockworkclyde.network.api.model

sealed class PagingState<out T> {
    object Loading : PagingState<Nothing>()
    data class Content<T>(val data: T) : PagingState<T>()
    data class Error(val throwable: Throwable) : PagingState<Nothing>()
}
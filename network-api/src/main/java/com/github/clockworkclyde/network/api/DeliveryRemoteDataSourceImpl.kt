package com.github.clockworkclyde.network.api

import com.github.clockworkclyde.network.api.interactor.FoodApi
import com.github.clockworkclyde.network.api.model.MenuItemDto
import com.github.clockworkclyde.network.api.model.PagingState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import java.net.UnknownHostException
import javax.inject.Inject

class DeliveryRemoteDataSourceImpl @Inject constructor(private val api: FoodApi) {

    private val _state =
        MutableStateFlow<PagingState<HashMap<String, List<MenuItemDto>>>>(PagingState.Loading)
    val state = _state.asStateFlow()

    suspend fun initLoading(categories: List<String>) {
        if (_state.value is PagingState.Loading) {
            coroutineScope {
                try {
                    val categoriesContent = hashMapOf<String, List<MenuItemDto>>()
                    val runningTasks = categories.map { category ->
                        async {
                            val response = api.getMenuItems(query = category).list
                            categoriesContent[category] = response
                        }
                    }
                    runningTasks.awaitAll()
                    _state.emit(PagingState.Content(categoriesContent))
                } catch (e: UnknownHostException) {
                    _state.value = PagingState.Error(e)
                }
            }
        }
    }

}
package com.github.clockworkclyde.basedeliverymvvm.layers.database

import com.github.clockworkclyde.basedeliverymvvm.layers.database.dao.OrderCartDao
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.cart.OrderCartItem
import com.github.clockworkclyde.network.api.model.PagingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderCartLocalDataSourceImpl(private val orderDao: OrderCartDao) {

    private val _dataOrderCartStateFlow =
        MutableStateFlow<PagingState<Flow<List<OrderCartItem>>>>(PagingState.Loading)
    val dataOrderCartStateFlow = _dataOrderCartStateFlow.asStateFlow()

    private val _cartPriceStateFlow =
        MutableStateFlow(value = 0.00)
    val cartPriceStateFlow = _cartPriceStateFlow.asStateFlow()


    suspend fun insertOrderCartItem(item: OrderCartItem) {
        orderDao.insertOrderCartItem(item)
    }

    suspend fun deleteOrderCartItemById(id: Int) {
        orderDao.deleteOrderCartItem(itemId = id)
    }

}
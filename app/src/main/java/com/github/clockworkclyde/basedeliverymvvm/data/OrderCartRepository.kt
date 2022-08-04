package com.github.clockworkclyde.basedeliverymvvm.data

import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuItem
import com.github.clockworkclyde.basedeliverymvvm.database.OrderCartLocalDataSourceImpl
import com.github.clockworkclyde.basedeliverymvvm.database.entities.cart.OrderCartItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.cart.OrderProductItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderCartRepository @Inject constructor(private val localDataSource: OrderCartLocalDataSourceImpl) {

    fun getOrderCart(): Flow<List<OrderProductItem>> = localDataSource.data().map { entities ->
        mapEntitiesToOrder(entities)
    }

    suspend fun addToOrderCart(item: MenuItem) {
        localDataSource.insertOrderCartItem(mapUiToEntity(item))
    }

    suspend fun deleteFromOrderCart(id: Long) {
        localDataSource.deleteOrderCartItemById(id)
    }

    suspend fun updateAmountInOrderById(id: Long, amount: Int) {
        localDataSource.updateAmountInOrderById(id, amount)
    }

    private fun mapEntitiesToOrder(items: List<OrderCartItem>) = items.map {
        OrderProductItem(
            id = it.id,
            title = it.title,
            image = it.imageUrl,
            price = it.price,
            servingSize = it.servingSize,
            quantity = it.count
        )
    }
    private fun mapUiToEntity(item: MenuItem) = OrderCartItem(
        id = item.id,
        title = item.title,
        imageUrl = item.image,
        servingSize = item.servingSize,
        price = item.price
    )
}
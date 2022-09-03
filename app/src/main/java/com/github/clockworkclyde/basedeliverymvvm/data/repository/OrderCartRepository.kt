package com.github.clockworkclyde.basedeliverymvvm.data.repository

import com.github.clockworkclyde.models.ui.menu.DishItem
import com.github.clockworkclyde.basedeliverymvvm.data.datasource.OrderCartLocalDataSourceImpl
import com.github.clockworkclyde.models.local.cart.OrderCartEntity
import com.github.clockworkclyde.models.ui.cart.OrderProductItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderCartRepository @Inject constructor(private val localDataSource: OrderCartLocalDataSourceImpl) {

    fun getOrderCart(): Flow<List<OrderProductItem>> = localDataSource.data().map { entities ->
        mapEntitiesToOrder(entities)
    }

    suspend fun addToOrderCart(item: DishItem) {
        localDataSource.insertOrderCartItem(mapUiToEntity(item))
    }

    suspend fun deleteFromOrderCart(id: Long) {
        localDataSource.deleteOrderCartItemById(id)
    }

    suspend fun updateAmountInOrderById(id: Long, amount: Int) {
        localDataSource.updateAmountInOrderById(id, amount)
    }

    private fun mapEntitiesToOrder(items: List<OrderCartEntity>) = items.map {
        OrderProductItem(
            id = it.id,
            title = it.title,
            image = it.imageUrl,
            price = it.price,
            servingSize = it.servingSize,
            quantity = it.count
        )
    }
    private fun mapUiToEntity(item: DishItem) = OrderCartEntity(
        id = item.id,
        title = item.title,
        imageUrl = item.image,
        servingSize = item.servingSize,
        price = item.price
    )
}
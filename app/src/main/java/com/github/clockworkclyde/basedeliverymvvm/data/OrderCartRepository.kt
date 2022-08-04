package com.github.clockworkclyde.basedeliverymvvm.data

import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuItemUiModel
import com.github.clockworkclyde.basedeliverymvvm.database.OrderCartLocalDataSourceImpl
import com.github.clockworkclyde.basedeliverymvvm.database.entities.cart.OrderCartItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.cart.OrderProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderCartRepository @Inject constructor(private val localDataSource: OrderCartLocalDataSourceImpl) {

    fun getOrderCart(): Flow<List<OrderProduct>> = localDataSource.data().map { entities ->
        mapEntitiesToOrder(entities)
    }

    suspend fun addToOrderCart(item: MenuItemUiModel) {
        localDataSource.insertOrderCartItem(mapUiToEntity(item))
    }

    suspend fun deleteFromOrderCart(id: Long) {
        localDataSource.deleteOrderCartItemById(id)
    }

    suspend fun updateAmountInOrderById(id: Long, amount: Int) {
        localDataSource.updateAmountInOrderById(id, amount)
    }

    private fun mapEntitiesToOrder(items: List<OrderCartItem>) = items.map {
        OrderProduct(
            id = it.id,
            title = it.title,
            image = it.imageUrl,
            price = it.price,
            servingSize = it.servingSize,
            quantity = it.count
        )
    }
    private fun mapUiToEntity(item: MenuItemUiModel) = OrderCartItem(
        id = item.id,
        title = item.title,
        imageUrl = item.image,
        servingSize = item.servingSize,
        price = item.price
    )
}
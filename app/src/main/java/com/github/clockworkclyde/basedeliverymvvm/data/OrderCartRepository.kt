package com.github.clockworkclyde.basedeliverymvvm.data

import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuItemUiModel
import com.github.clockworkclyde.basedeliverymvvm.database.OrderCartLocalDataSourceImpl
import com.github.clockworkclyde.basedeliverymvvm.database.entities.cart.OrderCartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderCartRepository @Inject constructor(private val localDataSource: OrderCartLocalDataSourceImpl) {

    fun getOrderCart(): Flow<List<MenuItemUiModel>> = localDataSource.data().map { entities ->
        mapEntitiesToUi(entities)
    }

    suspend fun addToOrderCart(item: MenuItemUiModel) {
        localDataSource.insertOrderCartItem(mapUiToEntity(item))
    }

    suspend fun deleteFromOrderCart(id: Long) {
        localDataSource.deleteOrderCartItemById(id)
    }

    private fun mapEntitiesToUi(items: List<OrderCartItem>) = items.map {
        MenuItemUiModel(
            id = it.id,
            title = it.title,
            image = it.imageUrl,
            price = it.price,
            servingSize = it.servingSize
        )
    }

    private fun mapUiToEntity(item: MenuItemUiModel) = OrderCartItem(
        id = item.id,
        title = item.title,
        imageUrl = item.image,
        servingSize = item.servingSize,
        price = item.price,
        totalAmount = 1 // todo counter
    )
}
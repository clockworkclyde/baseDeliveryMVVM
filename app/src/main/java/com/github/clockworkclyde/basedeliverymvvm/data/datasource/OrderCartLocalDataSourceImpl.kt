package com.github.clockworkclyde.basedeliverymvvm.data.datasource

import com.github.clockworkclyde.basedeliverymvvm.providers.database.OrderCartDao
import com.github.clockworkclyde.models.local.cart.OrderCartEntity
import javax.inject.Inject

class OrderCartLocalDataSourceImpl @Inject constructor(private val orderDao: OrderCartDao) {

    fun data() = orderDao.dataOrderCartItems()

    suspend fun insertOrderCartItem(item: OrderCartEntity) {
        orderDao.insertOrderCartItem(item)
    }

    suspend fun deleteOrderCartItemById(id: Long) {
        orderDao.deleteOrderCartItem(itemId = id)
    }

    suspend fun updateAmountInOrderById(id: Long, amount: Int) {
        orderDao.updateQuantityInOrder(id, amount)
    }
}
package com.github.clockworkclyde.basedeliverymvvm.database

import com.github.clockworkclyde.basedeliverymvvm.database.dao.OrderCartDao
import com.github.clockworkclyde.basedeliverymvvm.database.entities.cart.OrderCartItem
import javax.inject.Inject

class OrderCartLocalDataSourceImpl @Inject constructor(private val orderDao: OrderCartDao) {

    fun data() = orderDao.dataOrderCartItems()

    suspend fun insertOrderCartItem(item: OrderCartItem) {
        orderDao.insertOrderCartItem(item)
    }

    suspend fun deleteOrderCartItemById(id: Long) {
        orderDao.deleteOrderCartItem(itemId = id)
    }

    suspend fun updateAmountInOrderById(id: Long, amount: Int) {
        orderDao.updateQuantityInOrder(id, amount)
    }
}
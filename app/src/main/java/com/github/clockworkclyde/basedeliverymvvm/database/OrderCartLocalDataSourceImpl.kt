package com.github.clockworkclyde.basedeliverymvvm.database

import com.github.clockworkclyde.basedeliverymvvm.database.dao.OrderCartDao
import com.github.clockworkclyde.basedeliverymvvm.database.entities.cart.OrderCartItem

class OrderCartLocalDataSourceImpl(private val orderDao: OrderCartDao) {

    suspend fun insertOrderCartItem(item: OrderCartItem) {
        orderDao.insertOrderCartItem(item)
    }

    suspend fun deleteOrderCartItemById(id: Int) {
        orderDao.deleteOrderCartItem(itemId = id)
    }

}
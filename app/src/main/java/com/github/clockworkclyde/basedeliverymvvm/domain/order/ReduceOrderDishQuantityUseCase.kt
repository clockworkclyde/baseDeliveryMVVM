package com.github.clockworkclyde.basedeliverymvvm.domain.order

import com.github.clockworkclyde.models.local.cart.OrderDishesEntity
import com.github.clockworkclyde.models.local.cart.OrderDishesPref
import com.google.gson.Gson
import javax.inject.Inject

class ReduceOrderDishQuantityUseCase @Inject constructor(private val gson: Gson) {

    operator fun invoke(id: Long, additionTime: Long): Boolean {
        OrderDishesPref.orderDishesEntities
            .map { gson.fromJson(it, OrderDishesEntity::class.java) }
            .mapNotNull {
                if (it.id == id && it.additionTime == additionTime) {
                    if (it.quantity > 1) {
                        gson.toJson(it.copy(quantity = it.quantity - 1))
                    } else {
                        // deleting item from orderCartPref
                        null
                    }
                } else {
                    gson.toJson(it)
                }
            }
            .let {
                OrderDishesPref.orderDishesEntities.clear()
                return OrderDishesPref.orderDishesEntities.addAll(it)
            }
    }
}
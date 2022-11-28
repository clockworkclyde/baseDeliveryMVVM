package com.github.clockworkclyde.basedeliverymvvm.domain.order

import com.github.clockworkclyde.models.local.cart.OrderDishesEntity
import com.github.clockworkclyde.models.local.cart.OrderDishesPref
import com.google.gson.Gson
import javax.inject.Inject

class DeleteFromOrderCartUseCase @Inject constructor(private val gson: Gson) {

    operator fun invoke(id: Long): Boolean {
        OrderDishesPref.orderDishesEntities
            .map { gson.fromJson(it, OrderDishesEntity::class.java) }
            .mapNotNull {
                if (it.id == id) {
                    null
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
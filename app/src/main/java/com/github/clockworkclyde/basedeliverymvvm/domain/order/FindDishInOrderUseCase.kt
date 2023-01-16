package com.github.clockworkclyde.basedeliverymvvm.domain.order

import com.github.clockworkclyde.models.local.cart.OrderDishesEntity
import com.github.clockworkclyde.models.local.cart.OrderDishesPref
import com.github.clockworkclyde.models.ui.dishes.extra.DishExtra
import com.google.gson.Gson
import javax.inject.Inject

class FindDishInOrderUseCase @Inject constructor(private val gson: Gson) {

    operator fun invoke(id: Long, extras: List<DishExtra>) =
        OrderDishesPref.orderDishesEntities
            .map { gson.fromJson(it, OrderDishesEntity::class.java) }
            .let { list ->
                list.find { entity ->
                    entity.id == id && entity.extrasIds == extras.associate { it.id to it.quantity }
                }
            }
}
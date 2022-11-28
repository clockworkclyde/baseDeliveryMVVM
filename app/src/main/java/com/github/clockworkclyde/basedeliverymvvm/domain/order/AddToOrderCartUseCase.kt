package com.github.clockworkclyde.basedeliverymvvm.domain.order

import com.github.clockworkclyde.models.local.cart.OrderDishesEntity
import com.github.clockworkclyde.models.local.cart.OrderDishesPref
import com.github.clockworkclyde.models.ui.order.DishExtra
import com.google.gson.Gson
import timber.log.Timber
import javax.inject.Inject

class AddToOrderCartUseCase @Inject constructor(
    private val gson: Gson,
    private val findDishInOrderCart: FindDishInOrderUseCase
) {

    operator fun invoke(
        id: Long,
        extras: List<DishExtra> = emptyList(),
        quantity: Int
    ): Boolean {
        findDishInOrderCart(id, extras)?.let { entity ->
            OrderDishesPref.orderDishesEntities.remove(gson.toJson(entity))
            entity.copy(quantity = quantity + entity.quantity).let {
                return OrderDishesPref.orderDishesEntities.add(gson.toJson(it))
            }
        }
        val item =
            OrderDishesEntity(
                id = id,
                quantity = quantity,
                additionTime = System.currentTimeMillis(),
                extrasIds = extras.associate { it.id to it.quantity }
            )
        return OrderDishesPref.orderDishesEntities.add(gson.toJson(item))
    }
}
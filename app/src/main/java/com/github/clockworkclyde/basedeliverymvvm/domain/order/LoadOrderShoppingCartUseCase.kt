package com.github.clockworkclyde.basedeliverymvvm.domain.order

import com.github.clockworkclyde.basedeliverymvvm.data.repository.DishesRepository
import com.github.clockworkclyde.models.local.cart.OrderDishesEntity
import com.github.clockworkclyde.models.ui.order.OrderDish
import javax.inject.Inject

class LoadOrderShoppingCartUseCase @Inject constructor(private val dishesRepository: DishesRepository) {

    suspend operator fun invoke(list: List<OrderDishesEntity>): List<OrderDish> {
        return list.groupBy { it.toKey() }.values.mapNotNull {
            dishesRepository.search(it.last().id)?.let { dish ->
                OrderDish(
                    dish = dish,
                    quantity = it.sumOf { item -> item.quantity },
                    additionTime = it.last().additionTime,
                    extras = dishesRepository.searchExtras(dish.categoryId, it.last().extrasIds)
                )
            }
        }
    }
}

data class Key(val id: Long, val extras: Set<Int>)

private fun OrderDishesEntity.toKey() = Key(this.id, this.extrasIds.keys)
package com.github.clockworkclyde.basedeliverymvvm.domain.dishes

import com.github.clockworkclyde.basedeliverymvvm.data.repository.DishesRepository
import com.github.clockworkclyde.basedeliverymvvm.di.FoodExtrasData
import com.github.clockworkclyde.models.ui.order.DishExtra
import javax.inject.Inject

class GetExtrasForCategoryUseCase @Inject constructor(private val dishesRepository: DishesRepository) {

    operator fun invoke(id: Int): List<DishExtra> {
        return dishesRepository.getExtrasForCategory(id)
            .map { it.convertTo() }
    }

    private fun FoodExtrasData.convertTo(): DishExtra = DishExtra(id = id, title = title, price = price)
}
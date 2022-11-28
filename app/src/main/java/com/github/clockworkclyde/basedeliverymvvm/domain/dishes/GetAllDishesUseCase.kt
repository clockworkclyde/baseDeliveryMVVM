package com.github.clockworkclyde.basedeliverymvvm.domain.dishes

import com.github.clockworkclyde.basedeliverymvvm.data.repository.DishesRepository
import com.github.clockworkclyde.models.ui.dishes.DishItem
import javax.inject.Inject

class GetAllDishesUseCase @Inject constructor(private val dishesRepository: DishesRepository) {

    suspend operator fun invoke(): List<DishItem> {
        return dishesRepository.getAllDishes()
    }
}
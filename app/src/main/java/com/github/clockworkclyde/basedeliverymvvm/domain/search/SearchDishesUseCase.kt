package com.github.clockworkclyde.basedeliverymvvm.domain.search

import com.github.clockworkclyde.basedeliverymvvm.data.repository.DishesRepository
import com.github.clockworkclyde.models.ui.dishes.DishItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchDishesUseCase @Inject constructor(private val dishesRepository: DishesRepository) {

    operator fun invoke(query: String): Flow<List<DishItem>> {
        return dishesRepository.search(query)
    }
}
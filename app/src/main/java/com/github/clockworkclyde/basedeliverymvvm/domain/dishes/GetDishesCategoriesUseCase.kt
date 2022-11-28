package com.github.clockworkclyde.basedeliverymvvm.domain.dishes

import com.github.clockworkclyde.basedeliverymvvm.data.repository.DishesRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetDishesCategoriesUseCase @Inject constructor(private val dishesRepository: DishesRepository) {

    @OptIn(FlowPreview::class)
    operator fun invoke() = dishCategories().flatMapConcat {
        flow {
            dishesRepository.loadDishesByCategories()
            emitAll(dishesRepository.getDishes())
        }
    }

    private fun dishCategories() = flow {
        emit(dishesRepository.getCategories())
    }
}
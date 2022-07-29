package com.github.clockworkclyde.basedeliverymvvm.domain.usecases

import com.github.clockworkclyde.basedeliverymvvm.data.OrderCartRepository
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuItemUiModel
import javax.inject.Inject

class AddToOrderCartUseCase @Inject constructor(private val orderRepository: OrderCartRepository) {

    suspend fun execute(item: MenuItemUiModel) {
        orderRepository.addToOrderCart(item)
    }
}
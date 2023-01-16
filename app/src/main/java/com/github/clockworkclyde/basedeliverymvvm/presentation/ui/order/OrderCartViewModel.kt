package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order

import com.github.clockworkclyde.basedeliverymvvm.domain.order.AddToOrderCartUseCase
import com.github.clockworkclyde.basedeliverymvvm.domain.order.ReduceOrderDishQuantityUseCase
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseViewModel
import com.github.clockworkclyde.models.ui.dishes.extra.DishExtra
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderCartViewModel @Inject constructor(
   private val reduceOrderDishQuantityById: ReduceOrderDishQuantityUseCase,
   private val addToOrderCartUseCase: AddToOrderCartUseCase
) : BaseViewModel() {

    fun updateItemByAction(
       id: Long,
       action: QuantityButtonAction,
       extras: List<DishExtra>,
       additionalTime: Long
    ) {
        when (action) {
           QuantityButtonAction.LESS -> reduceQuantityInOrderById(id, additionalTime)
           QuantityButtonAction.MORE -> addToOrderCart(id, extras)
        }
    }

    fun addToOrderCart(
        id: Long,
        extras: List<DishExtra> = emptyList(),
        quantity: Int = 1
    ): Boolean {
        return addToOrderCartUseCase(id, extras, quantity)
    }

    private fun reduceQuantityInOrderById(id: Long, additionTime: Long) {
        reduceOrderDishQuantityById(id, additionTime)
    }
}
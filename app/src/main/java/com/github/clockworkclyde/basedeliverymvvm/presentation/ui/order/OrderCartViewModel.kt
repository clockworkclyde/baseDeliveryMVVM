package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order

import com.github.clockworkclyde.basedeliverymvvm.domain.order.AddToOrderCartUseCase
import com.github.clockworkclyde.basedeliverymvvm.domain.order.DeleteFromOrderCartUseCase
import com.github.clockworkclyde.basedeliverymvvm.domain.order.ReduceOrderDishQuantityUseCase
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseViewModel
import com.github.clockworkclyde.models.ui.order.DishExtra
import com.github.clockworkclyde.models.ui.order.DishExtraEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrderCartViewModel @Inject constructor(
    private val reduceOrderDishQuantityById: ReduceOrderDishQuantityUseCase,
    private val addToOrderCartUseCase: AddToOrderCartUseCase,
    private val deleteFromOrderCart: DeleteFromOrderCartUseCase
) : BaseViewModel() {

    fun updateItemByAction(
        id: Long,
        action: QuantityButtonAction,
        extras: List<DishExtraEntity>,
        additionalTime: Long
    ) {
        when (action) {
            QuantityButtonAction.LESS -> reduceQuantityInOrderById(id, additionalTime)
            QuantityButtonAction.MORE -> addToOrderCart(id, extras.map { it.convertTo() })
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
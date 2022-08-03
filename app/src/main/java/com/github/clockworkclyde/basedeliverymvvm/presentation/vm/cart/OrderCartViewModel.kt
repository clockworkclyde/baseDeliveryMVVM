package com.github.clockworkclyde.basedeliverymvvm.presentation.vm.cart

import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.data.OrderCartRepository
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.cart.OrderProduct
import com.github.clockworkclyde.basedeliverymvvm.presentation.vm.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderCartViewModel @Inject constructor(
    private val orderRepository: OrderCartRepository
) : BaseViewModel() {

    private val _data = MutableStateFlow<List<OrderProduct>>(emptyList())
    val data = _data.asStateFlow()

    init {
        viewModelScope.launch {
            orderRepository.getOrderCart().collect {
                _data.value = it
            }
        }
    }

    fun changeItemQuantity(id: Long, value: Int) {
        if (value == 0) {
            deleteById(id)
            return
        }
        updateQuantityInOrderById(id, value)
    }

    private fun updateQuantityInOrderById(id: Long, quantity: Int) {
        viewModelScope.launch {
            orderRepository.updateAmountInOrderById(id, quantity)
        }
    }

    private fun deleteById(id: Long) {
        viewModelScope.launch {
            orderRepository.deleteFromOrderCart(id)
        }
    }
}
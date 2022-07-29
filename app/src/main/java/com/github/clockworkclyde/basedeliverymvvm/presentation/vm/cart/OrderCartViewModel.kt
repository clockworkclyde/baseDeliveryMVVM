package com.github.clockworkclyde.basedeliverymvvm.presentation.vm.cart

import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.data.OrderCartRepository
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuItemUiModel
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

    private val _data = MutableStateFlow<List<MenuItemUiModel>>(emptyList())
    val data = _data.asStateFlow()

    init {
        viewModelScope.launch {
            orderRepository.getOrderCart().collect {
                _data.value = it
            }
        }
    }
}
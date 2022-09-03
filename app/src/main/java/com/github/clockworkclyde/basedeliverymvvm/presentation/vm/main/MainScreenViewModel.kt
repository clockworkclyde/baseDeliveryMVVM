package com.github.clockworkclyde.basedeliverymvvm.presentation.vm.main

import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.data.repository.DeliveryRepository
import com.github.clockworkclyde.basedeliverymvvm.data.repository.OrderCartRepository
import com.github.clockworkclyde.basedeliverymvvm.presentation.vm.base.BaseViewModel
import com.github.clockworkclyde.models.ui.menu.DishItem
import com.github.clockworkclyde.models.ui.menu.DishesCategoryItem
import com.github.clockworkclyde.network.api.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val deliveryRepository: DeliveryRepository,
    private val orderRepository: OrderCartRepository
) : BaseViewModel() {

    private val _data = MutableStateFlow<ViewState<List<DishesCategoryItem>>>(ViewState.Loading)
    val data: StateFlow<ViewState<List<DishesCategoryItem>>> get() = _data

    @OptIn(FlowPreview::class)
    fun fetchLatestData() {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            deliveryRepository.getDishes()
                .flatMapMerge {
                    deliveryRepository.fetchDishes()
                    flow { emit(it) }
                }
                .collect {
                    if (it.isNotEmpty()) {
                        _data.value = ViewState.Success(it)
                    } else {
                        _data.value = ViewState.Empty
                    }
                }
        }
    }

    fun addToOrderCart(item: DishItem) {
        viewModelScope.launch { orderRepository.addToOrderCart(item) }
    }
}
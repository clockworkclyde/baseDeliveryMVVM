package com.github.clockworkclyde.basedeliverymvvm.presentation.vm.search

import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.data.repository.DeliveryRepository
import com.github.clockworkclyde.basedeliverymvvm.data.repository.OrderCartRepository
import com.github.clockworkclyde.models.ui.menu.DishItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.vm.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val deliveryRepository: DeliveryRepository,
    private val orderRepository: OrderCartRepository
) : BaseViewModel() {

    private val _searchQuery = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val data = _searchQuery.flatMapLatest { query ->
        query?.let { deliveryRepository.search(query) } ?: emptyFlow()
    }

    fun search(query: String) {
        viewModelScope.launch { _searchQuery.value = query }
    }

    fun addToOrderCart(item: DishItem) {
        viewModelScope.launch { orderRepository.addToOrderCart(item) }
    }
}
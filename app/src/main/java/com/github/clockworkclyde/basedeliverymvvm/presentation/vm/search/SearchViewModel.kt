package com.github.clockworkclyde.basedeliverymvvm.presentation.vm.search

import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.data.repository.DeliveryRepository
import com.github.clockworkclyde.basedeliverymvvm.presentation.vm.base.BaseViewModel
import com.github.clockworkclyde.models.local.cart.OrderCartPref
import com.github.clockworkclyde.models.ui.menu.DishItem
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val deliveryRepository: DeliveryRepository
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
        val json = Gson().toJson(item)
        OrderCartPref.dishes.add(json)
    }
}
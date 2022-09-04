package com.github.clockworkclyde.basedeliverymvvm.presentation.vm.main

import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.data.repository.DeliveryRepository
import com.github.clockworkclyde.basedeliverymvvm.presentation.vm.base.BaseViewModel
import com.github.clockworkclyde.models.local.cart.OrderCartPref
import com.github.clockworkclyde.models.ui.menu.DishItem
import com.github.clockworkclyde.models.ui.menu.DishesCategoryItem
import com.github.clockworkclyde.network.api.ViewState
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val deliveryRepository: DeliveryRepository
) : BaseViewModel() {

    private val _data = MutableStateFlow<ViewState<List<DishesCategoryItem>>>(ViewState.Loading)
    val data: StateFlow<ViewState<List<DishesCategoryItem>>> get() = _data

    init {
        fetchLatestData()
    }

    fun fetchLatestData() {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            deliveryRepository.getDishes()
                .onStart { deliveryRepository.loadDishes() }
                .distinctUntilChanged()
                .collect {
                    if (it.isNotEmpty() && it.all { cat -> cat.categoryId != null }) {
                        _data.value = ViewState.Success(it)
                    } else {
                        _data.value = ViewState.Empty
                    }
                }
        }
    }

    fun addToOrderCart(item: DishItem) {
        val json = Gson().toJson(item)
        OrderCartPref.dishes.add(json)
    }
}
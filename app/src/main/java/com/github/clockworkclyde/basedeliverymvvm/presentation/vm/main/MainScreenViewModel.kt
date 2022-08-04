package com.github.clockworkclyde.basedeliverymvvm.presentation.vm.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.data.DeliveryRepository
import com.github.clockworkclyde.basedeliverymvvm.data.OrderCartRepository
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuCategoryItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.vm.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val deliveryRepository: DeliveryRepository,
    private val orderRepository: OrderCartRepository
) : BaseViewModel() {

    private val _data = MutableStateFlow<List<MenuCategoryItem>>(emptyList())
    val data = _data.asStateFlow()

    private fun initData() {
        viewModelScope.launch(errorHandler) {
            deliveryRepository.init(true)
        }
    }

    init {
        viewModelScope.launch {
            initData()
            deliveryRepository.data().collect {
                _data.value = it
            }
        }
    }

    fun addToOrderCart(item: MenuItem) {
        viewModelScope.launch { orderRepository.addToOrderCart(item) }
    }
}
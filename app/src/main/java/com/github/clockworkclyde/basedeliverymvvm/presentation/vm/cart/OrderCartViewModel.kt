package com.github.clockworkclyde.basedeliverymvvm.presentation.vm.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.chibatching.kotpref.livedata.asLiveData
import com.github.clockworkclyde.basedeliverymvvm.presentation.vm.base.BaseViewModel
import com.github.clockworkclyde.models.local.cart.OrderCartPref
import com.github.clockworkclyde.models.ui.cart.OrderProductItem
import com.github.clockworkclyde.models.ui.menu.DishItem
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderCartViewModel @Inject constructor() : BaseViewModel() {

    val orderDishes = OrderCartPref.asLiveData(OrderCartPref::dishes)

//    fun getOrderCart(): Flow<List<DishItem>> {
//        return flow { OrderCartPref.dishes.map { Gson().fromJson(it, DishItem::class.java) } }
//    }

    fun changeItemQuantity(id: Long, value: Int) {
        if (value > 0) {
            updateQuantityInOrderById(id, value)
        } else {
            deleteById(id)
        }
    }

    private fun updateQuantityInOrderById(id: Long, quantity: Int) {
        viewModelScope.launch {
//            orderRepository.updateAmountInOrderById(id, quantity)
        }
    }

    private fun deleteById(id: Long) {
        for (item in OrderCartPref.dishes) {
            if (item.contains(id.toString())) {
                OrderCartPref.dishes.remove(item)
            }
        }
    }
}
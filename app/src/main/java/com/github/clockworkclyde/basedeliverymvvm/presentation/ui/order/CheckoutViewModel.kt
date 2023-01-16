package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order

import androidx.lifecycle.*
import com.github.clockworkclyde.basedeliverymvvm.domain.order.GetOrderDishesEntityUseCase
import com.github.clockworkclyde.basedeliverymvvm.domain.order.LoadOrderShoppingCartUseCase
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseViewModel
import com.github.clockworkclyde.models.local.cart.OrderDishesEntity
import com.github.clockworkclyde.models.ui.address.AddressItem
import com.github.clockworkclyde.models.ui.base.ViewState
import com.github.clockworkclyde.models.ui.order.OrderDish
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val loadOrderDishes: LoadOrderShoppingCartUseCase,
    private val getOrderDishesEntity: GetOrderDishesEntityUseCase
) : BaseViewModel() {

    private val _orderDishes = MutableStateFlow<List<OrderDish>>(listOf())

    private val orderDishesEntity = liveData {
        emitSource(getOrderDishesEntity())
    }

    private val _totalPrice = MutableStateFlow<Int>(0)
    val totalPrice = _totalPrice.asLiveData()

    private val _nextButtonVisibility = MutableLiveData<Boolean>(false)
    val nextButtonVisibility: LiveData<Boolean> get() = _nextButtonVisibility

    private val _orderAddress = MutableStateFlow<AddressItem?>(null)
    val orderAddress = _orderAddress.asStateFlow()
    private val _orderPhone = MutableLiveData<String>()
    val orderPhone: LiveData<String> = _orderPhone
    private val _orderPaymentId = MutableStateFlow<Int>(PaymentMethod.CreditCard.id)
    val orderPaymentId = _orderPaymentId.asLiveData()

    fun setOrderAddress(address: AddressItem) {
        _orderAddress.value = address
    }

    fun setOrderPhone(phone: String) {
        _orderPhone.value = phone
    }

    fun setOrderPayment(id: Int) {
        _orderPaymentId.value = id
    }

    fun resetOrderDetails() {
        _orderAddress.value = null
        _orderPhone.value = null
        _orderPaymentId.value = PaymentMethod.CreditCard.id
    }

    val viewState = orderDishesEntity
        .asFlow()
        .distinctUntilChanged()
        .asLiveData()
        .switchMap { list ->
            liveData {
                emit(ViewState.Loading)
                initOrderItems(list)
                _orderDishes.collect { dishes ->
                    calculateTotalPrice(dishes)
                    if (dishes.isNotEmpty()) {
                        emit(ViewState.Success(dishes))
                    } else {
                        emit(ViewState.Empty)
                    }
                }
            }
        }

    init {
        viewModelScope.launch {
            _totalPrice.collect { value ->
                _nextButtonVisibility.postValue(value > 0)
            }
        }
    }

    private fun initOrderItems(list: List<OrderDishesEntity>) = viewModelScope.launch {
        _orderDishes.tryEmit(loadOrderDishes(list))
    }

    private fun calculateTotalPrice(list: List<OrderDish>) {
        if (list.isNotEmpty()) {
            val value =
                list.sumOf { item -> (item.dish.price + item.selectedExtras.sumOf { it.price * it.quantity }) * item.quantity }
            setTotalPrice(value)
        } else setTotalPrice(0)
    }

    private fun setTotalPrice(value: Int) {
        _totalPrice.tryEmit(value)
    }
}
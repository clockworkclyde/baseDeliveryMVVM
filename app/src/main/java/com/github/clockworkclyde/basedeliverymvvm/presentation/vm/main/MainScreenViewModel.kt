package com.github.clockworkclyde.basedeliverymvvm.presentation.vm.main

import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.data.DeliveryRepository
import com.github.clockworkclyde.basedeliverymvvm.domain.usecases.AddToOrderCartUseCase
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuCategoryUiModel
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuItemUiModel
import com.github.clockworkclyde.basedeliverymvvm.presentation.vm.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val deliveryRepository: DeliveryRepository,
    private val useCase: AddToOrderCartUseCase
) : BaseViewModel() {

    private val _data = MutableStateFlow<List<MenuCategoryUiModel>>(emptyList())
    val data = _data.asStateFlow()

    private fun initData() {
        viewModelScope.launch {
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

    fun addToOrderCart(item: MenuItemUiModel) {
        viewModelScope.launch { useCase.execute(item) }
    }
}
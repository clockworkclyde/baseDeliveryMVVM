package com.github.clockworkclyde.basedeliverymvvm.ui.vm.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.data.DeliveryRepository
import com.github.clockworkclyde.basedeliverymvvm.data.model.MenuCategoryUiModel
import com.github.clockworkclyde.basedeliverymvvm.ui.vm.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: DeliveryRepository
) : BaseViewModel() {

    private val _data = MutableStateFlow<List<MenuCategoryUiModel>>(emptyList())
    val data = _data.asStateFlow()

    private fun initData() {
        viewModelScope.launch {
            repository.init(true)
        }
    }

    init {
        viewModelScope.launch {
            initData()
            repository.data().collect {
                _data.value = it
                Log.e("data flow", it.toString())
            }
        }
    }
}
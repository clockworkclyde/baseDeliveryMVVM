package com.github.clockworkclyde.basedeliverymvvm.layers.ui.vm.main

import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.layers.data.DeliveryRepository
import com.github.clockworkclyde.basedeliverymvvm.layers.ui.vm.base.BaseViewModel
import com.github.clockworkclyde.basedeliverymvvm.layers.data.model.MenuCategoryUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: DeliveryRepository
) : BaseViewModel() {

    private val _data = MutableStateFlow<List<MenuCategoryUiModel>>(emptyList())
    val data = _data.asStateFlow()

    private val _searchQuery = MutableStateFlow("")


    init {
        viewModelScope.launch {
            withContext(Dispatchers.Default) { initData() }
            repository.data().collect { _data.value = it }
        }
    }

//    search in room, todo after room caching
//    fun searchMenuItems(query: String) {
//        _searchQuery.value = query
//    }

    private fun initData() {
        viewModelScope.launch {
            repository.init()
        }
    }

}
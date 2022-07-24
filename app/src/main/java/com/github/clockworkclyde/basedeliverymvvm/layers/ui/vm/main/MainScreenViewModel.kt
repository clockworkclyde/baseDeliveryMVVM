package com.github.clockworkclyde.basedeliverymvvm.layers.ui.vm.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.layers.data.DeliveryRepository
import com.github.clockworkclyde.basedeliverymvvm.layers.data.model.MenuCategoryUiModel
import com.github.clockworkclyde.basedeliverymvvm.layers.data.model.MenuItemProgressModel
import com.github.clockworkclyde.basedeliverymvvm.layers.ui.vm.base.BaseViewModel
import com.github.clockworkclyde.network.api.model.PagingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.UnknownHostException
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


//private suspend fun handleStates() {
//    repository.data().map { state ->
//        when (state) {
//            is PagingState.Loading -> {
//                val progressObjects = IntRange(1, 10).map { MenuItemProgressModel }
//                listOf(MenuCategoryUiModel("_", progressObjects))
//            }
//            is PagingState.Content -> state.data.map { (category, items) ->
//                MenuCategoryUiModel(
//                    category = category,
//                    items = items
//                )
//            }
//            is PagingState.Error -> throw UnknownHostException("Network mechanism error") //todo handler
//            else -> throw NullPointerException("wrong state exception for paging state $state")
//        }
//    }.collect {
//        _data.value = it
//        Log.e("data flow", it.toString())
//    }
//}
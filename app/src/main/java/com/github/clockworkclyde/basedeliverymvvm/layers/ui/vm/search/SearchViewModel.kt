package com.github.clockworkclyde.basedeliverymvvm.layers.ui.vm.search

import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.layers.data.DeliveryRepository
import com.github.clockworkclyde.basedeliverymvvm.layers.ui.vm.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: DeliveryRepository
): BaseViewModel() {

    private val _searchQuery = MutableStateFlow("")

    val data = _searchQuery.flatMapLatest { query -> repository.search(query) }

    fun search(query: String) {
        viewModelScope.launch {  _searchQuery.value = query }
    }

}
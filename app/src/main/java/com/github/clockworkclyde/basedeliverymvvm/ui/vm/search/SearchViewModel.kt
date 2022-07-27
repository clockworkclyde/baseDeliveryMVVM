package com.github.clockworkclyde.basedeliverymvvm.ui.vm.search

import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.data.DeliveryRepository
import com.github.clockworkclyde.basedeliverymvvm.ui.vm.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: DeliveryRepository
) : BaseViewModel() {

    private val _searchQuery = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val data = _searchQuery.flatMapLatest { query ->
        query?.let { repository.search(query) } ?: emptyFlow()
    }

    fun search(query: String) {
        viewModelScope.launch { _searchQuery.value = query }
    }
}
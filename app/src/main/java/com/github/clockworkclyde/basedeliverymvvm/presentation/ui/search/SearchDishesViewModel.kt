package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.search

import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.domain.dishes.GetAllDishesUseCase
import com.github.clockworkclyde.basedeliverymvvm.domain.search.SearchDishesUseCase
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseViewModel
import com.github.clockworkclyde.models.ui.dishes.DishItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchDishesViewModel @Inject constructor(
    private val searchDishes: SearchDishesUseCase,
    private val getAllDishes: GetAllDishesUseCase
) : BaseViewModel() {

    private val _searchQuery = MutableStateFlow<String>("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val data by lazy {
        _searchQuery.filter { it.trim().isNotEmpty() }
            .distinctUntilChanged()
            .flatMapLatest { query -> searchDishes(query) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), dishes)
    }
    
    private val dishes by lazy {
       val list = buildList<DishItem> {
          viewModelScope.launch {
             addAll(getAllDishes())
          }
       }
       return@lazy list
    }

    fun search(query: String) {
        _searchQuery.tryEmit(query)
    }
}
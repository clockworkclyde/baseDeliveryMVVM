package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.dishes

import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.domain.dishes.GetDishesCategoriesUseCase
import com.github.clockworkclyde.basedeliverymvvm.domain.order.GetOrderDishesEntityUseCase
import com.github.clockworkclyde.basedeliverymvvm.domain.order.LoadOrderShoppingCartUseCase
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseViewModel
import com.github.clockworkclyde.models.ui.base.ViewState
import com.github.clockworkclyde.models.ui.dishes.DishItem
import com.github.clockworkclyde.models.ui.dishes.DishesCategoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DishesViewModel @Inject constructor(
    private val loadOrderDishes: LoadOrderShoppingCartUseCase,
    private val getOrderDishesEntity: GetOrderDishesEntityUseCase,
    private val getDishesCategories: GetDishesCategoriesUseCase
) : BaseViewModel() {

    @OptIn(FlowPreview::class)
    val viewState: StateFlow<ViewState<List<DishesCategoryItem>>> by lazy {
        getDishesCategories()
            .flatMapConcat { categories ->
                flow {
                    emit(ViewState.Loading)
                    orderDishes().asFlow().collect { selected ->
                        val list = categories.map { cat ->
                            cat.items.map { item ->
                                if (item is DishItem) {
                                    selected.firstOrNull { it.dish == item } ?: item
                                } else item
                            }.let {
                                cat.copy(items = it)
                            }
                        }
                        if (list.isNotEmpty()) {
                            emit(ViewState.Success(list))
                        } else {
                            emit(ViewState.Empty)
                        }
                    }
                }
            }
            .catch { e -> emit(ViewState.Error(e)) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ViewState.Empty
            )
    }

    private fun orderDishes() = getOrderDishesEntity().switchMap {
        liveData {
            emit(loadOrderDishes.invoke(it))
        }
    }
}
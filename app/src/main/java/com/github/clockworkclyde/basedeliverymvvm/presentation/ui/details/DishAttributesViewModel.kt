package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.details

import androidx.lifecycle.*
import com.github.clockworkclyde.basedeliverymvvm.data.repository.DishesRepository
import com.github.clockworkclyde.basedeliverymvvm.domain.dishes.GetExtrasForCategoryUseCase
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseViewModel
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order.QuantityButtonAction
import com.github.clockworkclyde.models.ui.base.ViewState
import com.github.clockworkclyde.models.ui.order.DishExtra
import com.github.clockworkclyde.models.ui.order.DishExtraEntity
import com.github.clockworkclyde.models.ui.order.SelectedDishExtra
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DishAttributesViewModel @Inject constructor(
    private val getExtrasForCategory: GetExtrasForCategoryUseCase
) :
    BaseViewModel() {

    private val _data = MutableLiveData<List<DishExtra>>(listOf())

    /** Array-list containing selected dish's extras. **/
    private val _tempArray = MutableLiveData<MutableList<SelectedDishExtra>>(arrayListOf())

    private val _dishQuantity = MutableLiveData<Int>(1)
    val dishQuantity: LiveData<Int> = _dishQuantity

    fun editDishQuantity(action: QuantityButtonAction) {
        _dishQuantity.value?.let {
            when (action) {
                QuantityButtonAction.MORE -> {
                    setDishQuantity(it + 1)
                }
                QuantityButtonAction.LESS -> {
                    if (it > 1) {
                        setDishQuantity(it + 1)
                    }
                }
            }
        }
    }

    private fun setDishQuantity(quantity: Int) {
        _dishQuantity.postValue(quantity)
    }

    /** Emits fresh extras for dishes of specific category
     *  @param id category's id **/
    fun loadExtrasForCategory(id: Int) {
        getExtrasForCategory(id).let {
            _data.postValue(it)
        }
    }

    val viewState = _tempArray.switchMap {
        liveData {
            emit(ViewState.Loading)
            _data.map { loaded ->
                loaded.map { extra ->
                    extra.copy(
                        isSelected = it.contains(extra.convertTo()),
                        quantity = it.count { selected -> selected == extra.convertTo() })
                }
                    .let {
                        if (it.isNotEmpty()) {
                            ViewState.Success(it)
                        } else {
                            ViewState.Empty
                        }
                    }
            }.let { emitSource(it) }
        }
    }

    fun updateSelection(item: DishExtra, action: ExtraButtonAction = ExtraButtonAction.CLEAR) {
        item.convertTo().let {
            _tempArray.value?.apply {
                if (contains(it)) {
                    when (action) {
                        ExtraButtonAction.LESS -> remove(it)
                        ExtraButtonAction.MORE -> add(it)
                        ExtraButtonAction.CLEAR -> {
                            this.filter { item -> item != it }.let {
                                clear()
                                addAll(it)
                            }
                        }
                    }
                } else {
                    add(it)
                }
                _tempArray.postValue(this)
            }
        }
    }

    fun setSelectedExtras(extras: List<DishExtraEntity>) {
        val list = mutableListOf<SelectedDishExtra>()
        extras.onEach { extra ->
            val dishExtra = extra.convertTo()
            IntRange(1, extra.quantity).mapTo(list) {
                dishExtra.convertTo()
            }
        }
        _tempArray.postValue(list)
    }
}
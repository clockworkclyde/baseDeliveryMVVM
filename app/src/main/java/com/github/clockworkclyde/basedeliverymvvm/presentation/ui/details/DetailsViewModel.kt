package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.details

import androidx.lifecycle.*
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseViewModel
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order.QuantityButtonAction
import com.github.clockworkclyde.basedeliverymvvm.util.logg
import com.github.clockworkclyde.models.ui.dishes.extra.DishExtra
import com.github.clockworkclyde.models.ui.dishes.extra.ExtraCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
) : BaseViewModel() {

   private val dishExtras = MutableStateFlow<List<ExtraCategory>>(listOf())

   /** List containing selected dish's extras. **/
   private val _tempList = MutableStateFlow<List<DishExtra>>(listOf())

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
                  setDishQuantity(it - 1)
               }
            }
         }
      }
   }

   private fun setDishQuantity(quantity: Int) {
      _dishQuantity.postValue(quantity)
   }

   fun initDishExtras(items: List<ExtraCategory>, selected: List<DishExtra> = emptyList()) {
      val extras = items.map { cat ->
         cat.extras.map {
            it.copy(isSelected = selected.contains(it))
         }.let {
            cat.copy(extras = it)
         }
      }
      logg { extras.toString() }
      dishExtras.value = extras
   }

   @OptIn(ExperimentalCoroutinesApi::class)
   val extras = _tempList.flatMapLatest { selected ->
      dishExtras.map {
         it.map { cat ->
            val extras = cat.extras.map { extra ->
               selected.find { it == extra }?.copy(isSelected = true) ?: extra
            }
            cat.copy(extras = extras)
         }
      }
   }

   fun updateSelection(item: DishExtra, action: ExtraButtonAction = ExtraButtonAction.CLEAR) {
      item.let {
         _tempList.value.toMutableList().apply {
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
            _tempList.value = this
         }
      }
   }
}
package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.dishes

import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order.QuantityButtonAction
import com.github.clockworkclyde.basedeliverymvvm.util.BaseDiffUtilCallback
import com.github.clockworkclyde.models.ui.base.ListItem
import com.github.clockworkclyde.models.ui.order.OrderDish
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class DishesAdapter(onItemClickListener: OnDishItemClickListener, onQuantityClickListener: (OrderDish, QuantityButtonAction) -> Unit) :
    AsyncListDifferDelegationAdapter<ListItem>(BaseDiffUtilCallback()) {

    init {
        delegatesManager
            .addDelegate(MainScreenDelegates.dishesAdapterDelegate(onItemClickListener = onItemClickListener))
            .addDelegate(MainScreenDelegates.selectedDishesAdapterDelegate(onItemClickListener = onItemClickListener, onQuantityActionClick = onQuantityClickListener))
            .addDelegate(MainScreenDelegates.dishesProgressAdapterDelegate())
    }
}
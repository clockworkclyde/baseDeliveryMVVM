package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.search

import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.dishes.MainScreenDelegates
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.dishes.OnDishItemClickListener
import com.github.clockworkclyde.basedeliverymvvm.util.BaseDiffUtilCallback
import com.github.clockworkclyde.models.ui.base.ListItem
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class SearchDishesAdapter(onItemClickListener: OnDishItemClickListener) :
    AsyncListDifferDelegationAdapter<ListItem>(BaseDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(
            MainScreenDelegates.dishesAdapterDelegate(
                onItemClickListener
            )
        )
    }
}
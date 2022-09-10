package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.main

import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.MainScreenDelegates
import com.github.clockworkclyde.basedeliverymvvm.presentation.util.BaseDiffUtilCallback
import com.github.clockworkclyde.models.ui.base.ListItem
import com.github.clockworkclyde.models.ui.menu.DishItem
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class CategoryAdapter(onItemClickListener: (DishItem, MainScreenDelegates.ClickAction) -> Unit) :
    AsyncListDifferDelegationAdapter<ListItem>(BaseDiffUtilCallback()) {

    init {
        delegatesManager
            .addDelegate(MainScreenDelegates.dishesAdapterDelegate(onItemClickListener = onItemClickListener))
            .addDelegate(MainScreenDelegates.dishesProgressAdapterDelegate())
    }
}
package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.main

import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.MainScreenDelegates
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.base.ListItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.util.BaseDiffUtilCallback
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class CategoryAdapter(onItemClickListener: (MenuItem, MainScreenDelegates.ClickAction) -> Unit) :
    AsyncListDifferDelegationAdapter<ListItem>(BaseDiffUtilCallback()) {

    init {
        delegatesManager
            .addDelegate(MainScreenDelegates.menuItemsAdapterDelegate(onItemClickListener = onItemClickListener))
            .addDelegate(MainScreenDelegates.menuItemsProgressAdapterDelegate())
    }
}
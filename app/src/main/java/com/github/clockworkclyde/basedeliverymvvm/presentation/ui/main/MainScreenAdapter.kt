package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.main

import com.github.clockworkclyde.basedeliverymvvm.presentation.util.BaseDiffUtilCallback
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.base.ListItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuItemUiModel
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class MainScreenAdapter(
    onItemClickListener: (MenuItemUiModel, Int) -> Unit
) : AsyncListDifferDelegationAdapter<ListItem>(BaseDiffUtilCallback()) {

    init {
        delegatesManager
            .addDelegate(MainScreenDelegates.mainScreenAdapterDelegate(onItemClickListener = onItemClickListener))
            .addDelegate(MainScreenDelegates.mainScreenProgressAdapterDelegate())
    }
}
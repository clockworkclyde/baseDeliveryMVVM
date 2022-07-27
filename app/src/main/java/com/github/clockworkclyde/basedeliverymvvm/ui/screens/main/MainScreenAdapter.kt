package com.github.clockworkclyde.basedeliverymvvm.ui.screens.main

import com.github.clockworkclyde.basedeliverymvvm.ui.util.BaseDiffUtilCallback
import com.github.clockworkclyde.basedeliverymvvm.data.base.ListItem
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class MainScreenAdapter(
    //buttonClickListener: (MenuItemUiModel) -> Unit
) : AsyncListDifferDelegationAdapter<ListItem>(BaseDiffUtilCallback()) {

    init {
        delegatesManager
            .addDelegate(MainScreenDelegates.mainScreenAdapterDelegate())//buttonClickListener = buttonClickListener))
            .addDelegate(MainScreenDelegates.mainScreenProgressAdapterDelegate())
    }
}
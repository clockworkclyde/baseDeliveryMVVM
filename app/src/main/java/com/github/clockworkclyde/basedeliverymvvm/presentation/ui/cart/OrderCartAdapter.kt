package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.cart

import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.base.ListItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.main.MainScreenDelegates
import com.github.clockworkclyde.basedeliverymvvm.presentation.util.BaseDiffUtilCallback
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class OrderCartAdapter : AsyncListDifferDelegationAdapter<ListItem>(BaseDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(MainScreenDelegates.orderAdapterDelegate())
    }
}
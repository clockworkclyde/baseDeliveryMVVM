package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.cart

import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.base.ListItem
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.MainScreenDelegates
import com.github.clockworkclyde.basedeliverymvvm.presentation.util.BaseDiffUtilCallback
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class OrderCartAdapter(onButtonClickListener: (Long, Int) -> Unit) :
    AsyncListDifferDelegationAdapter<ListItem>(BaseDiffUtilCallback()) {

    init {
        delegatesManager.addDelegate(MainScreenDelegates.orderAdapterDelegate(onButtonClickListener = onButtonClickListener))
    }
}
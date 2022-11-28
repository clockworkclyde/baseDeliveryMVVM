package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.dishes

import android.os.Parcelable
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.order.QuantityButtonAction
import com.github.clockworkclyde.basedeliverymvvm.util.BaseDiffUtilCallback
import com.github.clockworkclyde.models.ui.base.ListItem
import com.github.clockworkclyde.models.ui.order.OrderDish
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class CategoryAdapter(
    onItemClickListener: OnDishItemClickListener,
    onQuantityActionClickListener: (OrderDish, QuantityButtonAction) -> Unit
) : AsyncListDifferDelegationAdapter<ListItem>(BaseDiffUtilCallback()) {

    init {
        delegatesManager
            .addDelegate(
                MainScreenDelegates.categoryAdapterDelegate(
                    onItemClickListener,
                    onQuantityActionClickListener
                )
            )
    }
}
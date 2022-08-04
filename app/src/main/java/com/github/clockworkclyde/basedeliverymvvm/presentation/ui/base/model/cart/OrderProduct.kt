package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.cart

import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.base.ListItem

data class OrderProduct(
    val id: Long,
    val title: String,
    val image: String,
    val price: Int,
    val servingSize: String,
    val quantity: Int = 1
): ListItem {
    override val itemId: Long = id
}

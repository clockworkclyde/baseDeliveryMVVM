package com.github.clockworkclyde.models.ui.cart

import com.github.clockworkclyde.models.ui.base.ListItem

data class OrderProductItem(
    val id: Long,
    val title: String,
    val image: String,
    val price: Int,
    val servingSize: String,
    val quantity: Int = 1
): ListItem {
    override val itemId: Long = id
}

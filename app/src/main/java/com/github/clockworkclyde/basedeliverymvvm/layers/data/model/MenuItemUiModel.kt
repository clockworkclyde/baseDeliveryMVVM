package com.github.clockworkclyde.basedeliverymvvm.layers.data.model

import com.github.clockworkclyde.basedeliverymvvm.layers.data.base.ListItem

data class MenuItemUiModel(
    val id: Long,
    val title: String,
    val image: String,
    val price: Double,
    val servingSize: String
): ListItem {
    override val itemId: Long = id
}
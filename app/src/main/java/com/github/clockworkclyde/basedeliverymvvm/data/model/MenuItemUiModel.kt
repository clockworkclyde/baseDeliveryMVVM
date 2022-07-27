package com.github.clockworkclyde.basedeliverymvvm.data.model

import com.github.clockworkclyde.basedeliverymvvm.data.base.ListItem

data class MenuItemUiModel(
    val id: Long,
    val title: String,
    val image: String,
    val price: Double,
    val servingSize: String
): ListItem {
    override val itemId: Long = id
}
package com.github.clockworkclyde.models.ui.order

import com.github.clockworkclyde.models.ui.base.ListItem
import com.github.clockworkclyde.models.ui.dishes.DishItem

data class OrderDish(
    val quantity: Int,
    val dish: DishItem,
    val additionTime: Long,
    val extras: List<DishExtraEntity>
) : ListItem {
    override val itemId: Long = dish.hashCode().toLong()
}
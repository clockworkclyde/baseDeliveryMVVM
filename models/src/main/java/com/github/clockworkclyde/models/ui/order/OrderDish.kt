package com.github.clockworkclyde.models.ui.order

import com.github.clockworkclyde.models.ui.base.ListItem
import com.github.clockworkclyde.models.ui.dishes.DishItem
import com.github.clockworkclyde.models.ui.dishes.extra.DishExtra

data class OrderDish(
   val quantity: Int,
   val dish: DishItem,
   val additionTime: Long,
   val selectedExtras: List<DishExtra> = emptyList()
) : ListItem {
    override val itemId: Long = dish.hashCode().toLong()
}



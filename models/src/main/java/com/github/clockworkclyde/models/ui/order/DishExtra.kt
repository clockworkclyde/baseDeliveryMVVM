package com.github.clockworkclyde.models.ui.order

import com.github.clockworkclyde.models.mappers.IConvertableTo
import com.github.clockworkclyde.models.ui.base.ListItem

data class DishExtra(
    val id: Int,
    val title: String = "",
    val price: Int,
    val isSelected: Boolean = false,
    val quantity: Int = 0
) : ListItem, IConvertableTo<SelectedDishExtra> {

    override val itemId: Long = id.toLong()

    override fun convertTo(): SelectedDishExtra {
        return SelectedDishExtra(id, title, price)
    }
}
package com.github.clockworkclyde.models.ui.menu

import com.github.clockworkclyde.models.ui.base.ListItem

data class DishesCategoryItem(
    val categoryId: Int?,
    val items: List<ListItem>
): ListItem {
    override val itemId: Long = items.hashCode().toLong()
}
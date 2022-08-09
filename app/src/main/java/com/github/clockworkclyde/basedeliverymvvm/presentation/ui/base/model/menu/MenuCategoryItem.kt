package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu

import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.base.ListItem

data class MenuCategoryItem(
    val title: String,
    val items: List<ListItem>
): ListItem {
    override val itemId: Long = items.hashCode().toLong()
}
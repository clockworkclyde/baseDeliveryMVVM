package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu

import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.base.ListItem

data class MenuCategoryUiModel(
    val category: String,
    val items: List<ListItem>
)
package com.github.clockworkclyde.basedeliverymvvm.layers.data.model

import com.github.clockworkclyde.basedeliverymvvm.layers.data.base.ListItem

data class MenuCategoryUiModel(
    val category: String,
    val items: List<ListItem>
)
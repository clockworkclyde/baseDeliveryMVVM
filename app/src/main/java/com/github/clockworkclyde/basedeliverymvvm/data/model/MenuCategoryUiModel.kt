package com.github.clockworkclyde.basedeliverymvvm.data.model

import com.github.clockworkclyde.basedeliverymvvm.data.base.ListItem

data class MenuCategoryUiModel(
    val category: String,
    val items: List<ListItem>
)
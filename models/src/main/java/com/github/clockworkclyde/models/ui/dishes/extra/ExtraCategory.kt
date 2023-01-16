package com.github.clockworkclyde.models.ui.dishes.extra

import com.github.clockworkclyde.models.ui.base.ListItem

sealed interface UiExtraCat

data class ExtraCategory(
   val title: String,
   val extras: List<DishExtra>,
) : ListItem, UiExtraCat {
   override val itemId: Long = extras.hashCode().toLong()
}

data class MultipleExtraCategory(
   val title: String,
   val extras: List<DishExtra>
) : ListItem, UiExtraCat {
   override val itemId: Long = extras.hashCode().toLong()
}
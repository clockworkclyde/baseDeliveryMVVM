package com.github.clockworkclyde.models.ui.dishes.extra

import android.os.Parcelable
import com.github.clockworkclyde.models.ui.base.ListItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class DishExtra(
   val id: Int,
   val title: String,
   val price: Int,
   val isSelected: Boolean = false,
   val quantity: Int = 0
) : ListItem, Parcelable {

   @IgnoredOnParcel
   override val itemId: Long = id.toLong()
}


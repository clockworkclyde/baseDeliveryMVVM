package com.github.clockworkclyde.models.ui.dishes

import android.os.Parcelable
import com.github.clockworkclyde.models.ui.base.ListItem
import com.github.clockworkclyde.models.ui.dishes.extra.ExtraCategory
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class DishItem(
   val id: Long,
   val title: String,
   val image: String,
   val price: Int,
   val servingSize: String,
   val categoryId: Int = 0,
   val extras: @RawValue List<ExtraCategory> = emptyList()
) : ListItem, Parcelable {
    @IgnoredOnParcel
    override val itemId: Long = id
}
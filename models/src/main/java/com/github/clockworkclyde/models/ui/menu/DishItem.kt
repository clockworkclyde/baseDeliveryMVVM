package com.github.clockworkclyde.models.ui.menu

import android.os.Parcelable
import com.github.clockworkclyde.models.ui.base.ListItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class DishItem(
    val id: Long,
    val title: String,
    val image: String,
    val price: Int,
    val servingSize: String,
    val categoryId: Int = 0

) : ListItem, Parcelable {
    @IgnoredOnParcel
    override val itemId: Long = id
}
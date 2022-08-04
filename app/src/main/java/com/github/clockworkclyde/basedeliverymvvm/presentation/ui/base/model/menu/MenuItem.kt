package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu

import android.os.Parcelable
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.base.ListItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItem(
    val id: Long,
    val title: String,
    val image: String,
    val price: Int,
    val servingSize: String
) : ListItem, Parcelable {
    @IgnoredOnParcel
    override val itemId: Long = id
}
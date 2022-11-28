package com.github.clockworkclyde.models.ui.order

import android.os.Parcelable
import com.github.clockworkclyde.models.mappers.IConvertableTo
import kotlinx.parcelize.Parcelize

@Parcelize
data class DishExtraEntity(
    val id: Int,
    val title: String,
    val quantity: Int,
    val price: Int
) : Parcelable, IConvertableTo<DishExtra> {
    override fun convertTo(): DishExtra = DishExtra(id = id, title = title, price = price, quantity = quantity, isSelected = true)
}
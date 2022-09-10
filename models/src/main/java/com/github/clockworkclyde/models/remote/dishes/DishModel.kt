package com.github.clockworkclyde.models.remote.dishes

import com.github.clockworkclyde.models.local.dishes.DishEntity
import com.github.clockworkclyde.models.mappers.IConvertableToWithParams
import com.google.gson.annotations.SerializedName

data class DishModel(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: String,
    @SerializedName("servingSize") val servingSize: String?
) : IConvertableToWithParams<DishEntity, Int> {

    override fun convertTo(param: Int): DishEntity {
        return DishEntity(
            id = id,
            title = title,
            imageUrl = image,
            price = title.length * 10,
            servingSize = servingSize ?: "",
            categoryId = param
        )
    }
}
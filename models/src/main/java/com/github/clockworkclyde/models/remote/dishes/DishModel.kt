package com.github.clockworkclyde.models.remote.dishes

import com.github.clockworkclyde.models.local.dishes.DishEntity
import com.github.clockworkclyde.models.mappers.IConvertableToWithParams
import com.github.clockworkclyde.models.ui.dishes.extra.ExtraCategory
import com.google.gson.annotations.SerializedName

data class DishModel(
   @SerializedName("id") val id: Long,
   @SerializedName("title") val title: String,
   @SerializedName("image") val image: String,
   @SerializedName("servingSize") val servingSize: String?
) : IConvertableToWithParams<DishEntity, Pair<Int, List<ExtraCategory>>> {

   override fun convertTo(param: Pair<Int, List<ExtraCategory>>): DishEntity {
      return DishEntity(
         id = id,
         title = title,
         imageUrl = image,
         price = title.length * 10,
         servingSize = servingSize ?: "",
         categoryId = param.first,
         extras = param.second
      )
   }
}
package com.github.clockworkclyde.models.remote.dishes

import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("menuItems") val list: List<DishModel>
)
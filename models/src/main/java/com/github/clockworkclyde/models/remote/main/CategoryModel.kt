package com.github.clockworkclyde.models.remote.main

import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("menuItems") val list: List<DishModel>
)
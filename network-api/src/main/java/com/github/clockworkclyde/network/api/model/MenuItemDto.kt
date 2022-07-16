package com.github.clockworkclyde.network.api.model

import com.google.gson.annotations.SerializedName

data class MenuItemDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: String,
    @SerializedName("servingSize") val servingSize: String?
)
package com.github.clockworkclyde.network.api.model

import com.google.gson.annotations.SerializedName

data class MenuResponse(
    @SerializedName("menuItems") val list: List<MenuItemDto>
)
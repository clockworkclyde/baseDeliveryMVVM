package com.github.clockworkclyde.network.api

import com.github.clockworkclyde.network.api.interactor.FoodApi
import com.github.clockworkclyde.network.api.model.MenuItemDto
import javax.inject.Inject

class DeliveryRemoteDataSourceImpl @Inject constructor(private val api: FoodApi) {

    suspend fun initLoading(category: String): List<MenuItemDto> {
        val response = api.getMenuItems(query = category)
        return response.list
    }

}
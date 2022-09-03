package com.github.clockworkclyde.network.api

import com.github.clockworkclyde.network.api.interactor.FoodApi
import com.github.clockworkclyde.models.remote.main.DishModel
import javax.inject.Inject

class DeliveryRemoteDataSourceImpl @Inject constructor(private val api: FoodApi) {

    suspend fun initLoading(category: String): List<DishModel> {
        return api.getDishes(query = category).list
    }

}
package com.github.clockworkclyde.network.api

import com.github.clockworkclyde.models.remote.dishes.DishModel
import com.github.clockworkclyde.network.api.interactor.DishesApi
import javax.inject.Inject

class DeliveryRemoteDataSourceImpl @Inject constructor(private val api: DishesApi) {

    suspend fun initLoading(category: String): List<DishModel> {
        return api.getDishes(query = category).list
    }

}
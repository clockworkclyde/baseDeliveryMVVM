package com.github.clockworkclyde.network.api.interactor

import com.github.clockworkclyde.models.remote.dishes.CategoryModel
import com.github.clockworkclyde.network.api.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface DishesApi {

    companion object {
        const val apiKey = BuildConfig.API_KEY
    }

    @Headers("x-api-key: $apiKey")
    @GET("food/menuItems/search")
    suspend fun getDishes(
        @Query("query") query: String = "pizza",
        @Query("offset") page: Int = 0,
        @Query("number") perPage: Int = 10
    ): CategoryModel
}
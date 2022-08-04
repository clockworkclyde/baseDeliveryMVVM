package com.github.clockworkclyde.network.api.interactor

import com.github.clockworkclyde.network.api.BuildConfig
import com.github.clockworkclyde.network.api.model.MenuResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FoodApi {

    companion object {
        const val apiKey = BuildConfig.API_KEY
    }

    @Headers("x-api-key: $apiKey")
    @GET("food/menuItems/search")
    suspend fun getMenuItems(
        @Query("query") query: String = "pizza",
        @Query("offset") page: Int = 0,
        @Query("number") perPage: Int = 10
    ): MenuResponse
}
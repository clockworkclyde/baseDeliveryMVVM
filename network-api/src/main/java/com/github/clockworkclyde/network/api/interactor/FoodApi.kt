package com.github.clockworkclyde.network.api.interactor

import com.github.clockworkclyde.network.api.model.MenuResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApi {

    @GET("food/menuItems/search")
    suspend fun getMenuItems(
        @Query("apiKey") apiKey: String = "e5e048c444f148398f49f2cd857e6fe1",
        @Query("query") query: String = "pizza",
        @Query("offset") page: Int = 0,
        @Query("number") perPage: Int = 10
    ): MenuResponse
}
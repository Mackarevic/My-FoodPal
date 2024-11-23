package com.example.myfoodpal.api

import com.example.myfoodpal.model.FoodResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodDataService {
    @GET("foods/search")
    fun searchFoods(
        @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Call<FoodResponse>
}

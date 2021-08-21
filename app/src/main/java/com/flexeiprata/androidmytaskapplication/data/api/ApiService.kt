package com.flexeiprata.androidmytaskapplication.data.api

import com.flexeiprata.androidmytaskapplication.data.models.JsonData
import com.flexeiprata.androidmytaskapplication.data.models.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/products")
    suspend fun getProducts(@Query("page") page : Int, @Query("page_size") pageSize: Int): Response<JsonData>

    @GET("/products/{idp}")
    suspend fun getProductById(@Path("idp") id : Int) : Product


}
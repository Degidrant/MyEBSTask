package com.flexeiprata.androidmytaskapplication.products.data.api

import com.flexeiprata.androidmytaskapplication.products.data.models.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsApiService {
    @GET("/products")
    suspend fun getProducts(@Query("page") page : Int, @Query("page_size") pageSize: Int, @Query("search") text: String): Response<ProductsResponse>
}
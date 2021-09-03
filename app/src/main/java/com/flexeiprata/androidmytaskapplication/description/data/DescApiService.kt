package com.flexeiprata.androidmytaskapplication.description.data

import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface DescApiService {

    @GET("/products/{idp}")
    suspend fun getProductById(@Path("idp") id : Int) : Product

}
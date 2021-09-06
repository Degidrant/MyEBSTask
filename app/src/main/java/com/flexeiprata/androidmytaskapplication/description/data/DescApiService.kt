package com.flexeiprata.androidmytaskapplication.description.data

import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface DescApiService {

    @GET("/products/{idp}")
    fun getProductById(@Path("idp") id : Int) : Single<Product>

}
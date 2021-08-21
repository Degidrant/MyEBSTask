package com.flexeiprata.androidmytaskapplication.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val BASE_URL = "http://mobile-shop-api.hiring.devebs.net"

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)

    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
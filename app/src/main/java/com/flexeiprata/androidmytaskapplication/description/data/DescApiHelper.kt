package com.flexeiprata.androidmytaskapplication.description.data

import javax.inject.Inject

class DescApiHelper @Inject constructor(private val apiService: DescApiService) {
    suspend fun getProductById(id : Int) = apiService.getProductById(id)
}
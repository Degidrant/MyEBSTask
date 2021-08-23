package com.flexeiprata.androidmytaskapplication.data.api


import com.flexeiprata.androidmytaskapplication.utils.PAGE_SIZE
import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiService: ApiService){
    suspend fun getProducts(page: Int) = apiService.getProducts(page, PAGE_SIZE)
    suspend fun getProductById(id : Int) = apiService.getProductById(id)
}
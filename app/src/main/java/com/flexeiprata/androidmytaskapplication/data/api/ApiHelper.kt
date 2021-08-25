package com.flexeiprata.androidmytaskapplication.data.api


import com.flexeiprata.androidmytaskapplication.utils.PAGE_SIZE
import javax.inject.Inject

class ApiHelper @Inject constructor(private val apiService: ApiService){
    suspend fun getProducts(page: Int, text: String) = apiService.getProducts(page, PAGE_SIZE, text)
    suspend fun getProductById(id : Int) = apiService.getProductById(id)
}
package com.flexeiprata.androidmytaskapplication.data.repository

import com.flexeiprata.androidmytaskapplication.data.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {
    suspend fun getProducts(page: Int) = apiHelper.getProducts(page)
    suspend fun getProductById(id : Int) = apiHelper.getProductById(id)

}
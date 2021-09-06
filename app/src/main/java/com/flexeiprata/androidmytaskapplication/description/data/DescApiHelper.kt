package com.flexeiprata.androidmytaskapplication.description.data

import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class DescApiHelper @Inject constructor(private val apiService: DescApiService) {
    fun getProductById(id : Int): Single<Product> = apiService.getProductById(id)
}
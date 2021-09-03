package com.flexeiprata.androidmytaskapplication.products.data.api


import com.flexeiprata.androidmytaskapplication.common.PAGE_SIZE
import javax.inject.Inject

class ProductsApiHelper @Inject constructor(private val productsApiService: ProductsApiService){
    suspend fun getProducts(page: Int, text: String) = productsApiService.getProducts(page, PAGE_SIZE, text)

}
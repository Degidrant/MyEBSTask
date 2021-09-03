package com.flexeiprata.androidmytaskapplication.products.domain

import com.flexeiprata.androidmytaskapplication.products.data.models.ProductsResponse
import retrofit2.Response

interface ProductsRepository {
    suspend fun getProducts(currentLoadingPageKey: Int, text: String): Response<ProductsResponse>
}
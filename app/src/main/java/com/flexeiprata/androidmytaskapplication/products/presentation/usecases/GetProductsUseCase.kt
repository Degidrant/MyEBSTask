package com.flexeiprata.androidmytaskapplication.products.presentation.usecases

import com.flexeiprata.androidmytaskapplication.products.data.models.ProductsResponse
import retrofit2.Response

interface GetProductsUseCase {
    suspend operator fun invoke(page: Int, text: String) : Response<ProductsResponse>
}
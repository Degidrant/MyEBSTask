package com.flexeiprata.androidmytaskapplication.products.domain

import com.flexeiprata.androidmytaskapplication.products.data.models.ProductsResponse
import com.flexeiprata.androidmytaskapplication.products.presentation.usecases.GetProductsUseCase
import retrofit2.Response
import javax.inject.Inject

class GetProductsUseCaseImpl @Inject constructor(private val productsRepository: ProductsRepository) : GetProductsUseCase{
    override suspend fun invoke(page: Int, text: String): Response<ProductsResponse> = productsRepository.getProducts(page, text)
}
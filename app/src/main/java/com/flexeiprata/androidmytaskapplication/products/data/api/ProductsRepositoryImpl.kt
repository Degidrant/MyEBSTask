package com.flexeiprata.androidmytaskapplication.products.data.api

import com.flexeiprata.androidmytaskapplication.products.domain.ProductsRepository
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(private val productsApiHelper: ProductsApiHelper) :
    ProductsRepository {
    override suspend fun getProducts(currentLoadingPageKey: Int, text: String) = productsApiHelper.getProducts(currentLoadingPageKey, text)

}
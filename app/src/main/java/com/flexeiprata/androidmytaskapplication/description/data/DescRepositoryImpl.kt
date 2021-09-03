package com.flexeiprata.androidmytaskapplication.description.data

import com.flexeiprata.androidmytaskapplication.description.domain.DescRepository
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import javax.inject.Inject

class DescRepositoryImpl @Inject constructor(private val apiHelper: DescApiHelper) : DescRepository {
    override suspend fun getProductById(id: Int): Product = apiHelper.getProductById(id)
}
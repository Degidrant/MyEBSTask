package com.flexeiprata.androidmytaskapplication.description.data

import com.flexeiprata.androidmytaskapplication.description.domain.DescRepository
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class DescRepositoryImpl @Inject constructor(private val apiHelper: DescApiHelper) : DescRepository {
    override fun getProductById(id: Int): Single<Product> = apiHelper.getProductById(id)
}
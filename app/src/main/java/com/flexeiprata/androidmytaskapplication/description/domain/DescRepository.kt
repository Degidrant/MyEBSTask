package com.flexeiprata.androidmytaskapplication.description.domain

import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Single

interface DescRepository {
     fun getProductById(id: Int) : Single<Product>
}
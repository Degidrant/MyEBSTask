package com.flexeiprata.androidmytaskapplication.description.domain

import com.flexeiprata.androidmytaskapplication.products.data.models.Product

interface DescRepository {
    suspend fun getProductById(id: Int) : Product
}
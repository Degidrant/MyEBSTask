package com.flexeiprata.androidmytaskapplication.description.presentation.usecases

import com.flexeiprata.androidmytaskapplication.products.data.models.Product

interface GetProductUseCase {
    suspend operator fun invoke(id: Int) : Product
}
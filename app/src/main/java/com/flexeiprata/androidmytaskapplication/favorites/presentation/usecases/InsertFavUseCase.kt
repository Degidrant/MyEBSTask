package com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases

import com.flexeiprata.androidmytaskapplication.products.data.models.Product

interface InsertFavUseCase {
    suspend operator fun invoke(product: Product)
}
package com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases

import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import kotlinx.coroutines.flow.Flow

interface GetAllFavsUseCase {
    operator fun invoke() : Flow<List<Product>>
}
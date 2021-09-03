package com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases

import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import kotlinx.coroutines.flow.Flow

interface GetFavByIdUseCase {
    operator fun invoke(id: Int) : Flow<Product?>
}
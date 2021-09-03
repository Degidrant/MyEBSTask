package com.flexeiprata.androidmytaskapplication.favorites.domain.usecaseimpl

import com.flexeiprata.androidmytaskapplication.favorites.domain.FavoritesRepository
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.DeleteFavUseCase
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import javax.inject.Inject

class DeleteFavUseCaseImpl @Inject constructor(private val favoritesRepository: FavoritesRepository) :
    DeleteFavUseCase {
    override suspend fun invoke(product: Product) {
        favoritesRepository.deleteFav(product)
    }
}
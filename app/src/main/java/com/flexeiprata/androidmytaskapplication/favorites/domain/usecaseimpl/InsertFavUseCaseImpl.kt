package com.flexeiprata.androidmytaskapplication.favorites.domain.usecaseimpl

import com.flexeiprata.androidmytaskapplication.favorites.domain.FavoritesRepository
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.InsertFavUseCase
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import javax.inject.Inject

class InsertFavUseCaseImpl @Inject constructor(private val favoritesRepository: FavoritesRepository) :
    InsertFavUseCase {
    override suspend fun invoke(product: Product) {
        favoritesRepository.insertFav(product)
    }
}
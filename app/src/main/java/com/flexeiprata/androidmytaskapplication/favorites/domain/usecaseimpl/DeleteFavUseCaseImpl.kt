package com.flexeiprata.androidmytaskapplication.favorites.domain.usecaseimpl

import com.flexeiprata.androidmytaskapplication.favorites.domain.FavoritesRepository
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.DeleteFavUseCase
import javax.inject.Inject

class DeleteFavUseCaseImpl @Inject constructor(private val favoritesRepository: FavoritesRepository) :
    DeleteFavUseCase {
    override suspend fun invoke(id: Int) {
        favoritesRepository.deleteFav(id)
    }
}
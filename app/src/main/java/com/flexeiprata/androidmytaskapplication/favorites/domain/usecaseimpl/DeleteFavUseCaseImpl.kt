package com.flexeiprata.androidmytaskapplication.favorites.domain.usecaseimpl

import com.flexeiprata.androidmytaskapplication.favorites.domain.FavoritesRepository
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.DeleteFavUseCase
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeleteFavUseCaseImpl @Inject constructor(private val favoritesRepository: FavoritesRepository) :
    DeleteFavUseCase {
    override fun invoke(id: Int): Completable =
        favoritesRepository.deleteFav(id)

}
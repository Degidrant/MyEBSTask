package com.flexeiprata.androidmytaskapplication.favorites.domain.usecaseimpl

import com.flexeiprata.androidmytaskapplication.favorites.domain.FavoritesRepository
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.InsertFavUseCase
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class InsertFavUseCaseImpl @Inject constructor(private val favoritesRepository: FavoritesRepository) :
    InsertFavUseCase {
    override fun invoke(product: Product): Completable {
       return favoritesRepository.insertFav(product)
    }
}
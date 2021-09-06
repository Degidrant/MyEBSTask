package com.flexeiprata.androidmytaskapplication.favorites.domain.usecaseimpl

import com.flexeiprata.androidmytaskapplication.favorites.domain.FavoritesRepository
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.GetFavByIdUseCase
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetFavByIDUseCaseImpl @Inject constructor(private val favoritesRepository: FavoritesRepository) :
    GetFavByIdUseCase {
    override operator fun invoke(id: Int): Single<Product?> = favoritesRepository.getFavByID(id)

}
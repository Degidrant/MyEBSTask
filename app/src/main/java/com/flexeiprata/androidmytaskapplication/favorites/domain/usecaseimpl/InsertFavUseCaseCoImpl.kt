package com.flexeiprata.androidmytaskapplication.favorites.domain.usecaseimpl

import com.flexeiprata.androidmytaskapplication.favorites.domain.FavoritesRepository
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.InsertFavUseCaseCo
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import javax.inject.Inject

class InsertFavUseCaseCoImpl @Inject constructor(private val favoritesRepository: FavoritesRepository) :
    InsertFavUseCaseCo {
    override fun invoke(product: Product) = favoritesRepository.insertFavCo(product)

}

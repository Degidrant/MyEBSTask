package com.flexeiprata.androidmytaskapplication.favorites.domain.usecaseimpl

import com.flexeiprata.androidmytaskapplication.favorites.domain.FavoritesRepository
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.ActualizeUseCase
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import javax.inject.Inject

class ActualizeUseCaseImpl @Inject constructor(private val favoritesRepository: FavoritesRepository) :
    ActualizeUseCase {
    override fun invoke(product: Product) = favoritesRepository.actualize(product)

}
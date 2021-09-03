package com.flexeiprata.androidmytaskapplication.favorites.domain.usecaseimpl

import com.flexeiprata.androidmytaskapplication.favorites.domain.FavoritesRepository
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.GetAllFavsUseCase
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFavsUseCaseImpl @Inject constructor(private val favoritesRepository: FavoritesRepository) :
    GetAllFavsUseCase {
    override fun invoke(): Flow<List<Product>> {
        return favoritesRepository.getAllFav()
    }
}
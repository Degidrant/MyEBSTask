package com.flexeiprata.androidmytaskapplication.favorites.domain.usecaseimpl

import com.flexeiprata.androidmytaskapplication.favorites.domain.FavoritesRepository
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.GetFavByIdUseCase
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavByIDUseCaseImpl @Inject constructor(private val favoritesRepository: FavoritesRepository) :
    GetFavByIdUseCase {
    override operator fun invoke(id: Int): Flow<Product?> {
        return favoritesRepository.getFavByID(id)
    }
}
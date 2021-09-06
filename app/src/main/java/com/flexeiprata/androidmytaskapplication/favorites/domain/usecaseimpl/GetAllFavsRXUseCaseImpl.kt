package com.flexeiprata.androidmytaskapplication.favorites.domain.usecaseimpl

import com.flexeiprata.androidmytaskapplication.favorites.domain.FavoritesRepository
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.GetAllFavsRXUseCase
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetAllFavsRXUseCaseImpl @Inject constructor(private val favoritesRepository: FavoritesRepository): GetAllFavsRXUseCase {
    override fun invoke(): Observable<List<Product>> {
        return favoritesRepository.getAllFavRX()
    }
}
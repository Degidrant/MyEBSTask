package com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases

import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Observable

interface GetAllFavsRXUseCase {
    operator fun invoke() : Observable<List<Product>>
}
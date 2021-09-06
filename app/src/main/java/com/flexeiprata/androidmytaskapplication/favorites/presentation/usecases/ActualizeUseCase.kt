package com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases

import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Completable

interface ActualizeUseCase {
    operator fun invoke(product: Product): Completable
}
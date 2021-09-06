package com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases

import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Single

interface GetFavByIdUseCase {
    operator fun invoke(id: Int) : Single<Product?>
}
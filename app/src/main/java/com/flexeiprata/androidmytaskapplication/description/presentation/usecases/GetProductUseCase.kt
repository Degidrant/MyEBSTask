package com.flexeiprata.androidmytaskapplication.description.presentation.usecases

import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Single

interface GetProductUseCase {
    operator fun invoke(id: Int) : Single<Product>
}
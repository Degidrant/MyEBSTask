package com.flexeiprata.androidmytaskapplication.cart.presentation

import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Completable

interface AddToCartUseCase {
    operator fun invoke(product: Product): Completable
}
package com.flexeiprata.androidmytaskapplication.cart.domain

import com.flexeiprata.androidmytaskapplication.cart.presentation.AddToCartUseCase
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class AddToCartUseCaseImpl @Inject constructor(private val cartRepository: CartRepository) : AddToCartUseCase {
    override fun invoke(product: Product): Completable {
        return cartRepository.addToCart(product)
    }
}
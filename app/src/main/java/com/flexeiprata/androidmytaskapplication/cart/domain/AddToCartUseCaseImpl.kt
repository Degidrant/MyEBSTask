package com.flexeiprata.androidmytaskapplication.cart.domain

import com.flexeiprata.androidmytaskapplication.cart.presentation.AddToCartUseCase
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import javax.inject.Inject

class AddToCartUseCaseImpl @Inject constructor(private val cartRepository: CartRepository) : AddToCartUseCase {
    override suspend fun invoke(product: Product) {
        return cartRepository.addToCart(product)
    }
}
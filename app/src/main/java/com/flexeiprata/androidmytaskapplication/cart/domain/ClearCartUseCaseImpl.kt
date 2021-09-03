package com.flexeiprata.androidmytaskapplication.cart.domain

import com.flexeiprata.androidmytaskapplication.cart.presentation.ClearCartUseCase
import javax.inject.Inject

class ClearCartUseCaseImpl @Inject constructor(private val cartRepository: CartRepository) : ClearCartUseCase {
    override suspend fun invoke() {
        cartRepository.clearCart()
    }
}
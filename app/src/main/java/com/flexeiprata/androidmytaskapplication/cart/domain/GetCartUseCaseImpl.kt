package com.flexeiprata.androidmytaskapplication.cart.domain

import com.flexeiprata.androidmytaskapplication.cart.presentation.GetCartUseCase
import javax.inject.Inject

class GetCartUseCaseImpl @Inject constructor(private val cartRepository: CartRepository): GetCartUseCase {
    override fun invoke() = cartRepository.getCart()
}
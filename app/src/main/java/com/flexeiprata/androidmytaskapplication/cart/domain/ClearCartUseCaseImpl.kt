package com.flexeiprata.androidmytaskapplication.cart.domain

import com.flexeiprata.androidmytaskapplication.cart.presentation.ClearCartUseCase
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class ClearCartUseCaseImpl @Inject constructor(private val cartRepository: CartRepository) : ClearCartUseCase {
    override fun invoke(): Completable = cartRepository.clearCart()

}
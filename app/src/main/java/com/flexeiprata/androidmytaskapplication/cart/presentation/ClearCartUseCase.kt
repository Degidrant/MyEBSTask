package com.flexeiprata.androidmytaskapplication.cart.presentation

import io.reactivex.rxjava3.core.Completable

interface ClearCartUseCase {
    operator fun invoke(): Completable
}
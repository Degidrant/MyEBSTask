package com.flexeiprata.androidmytaskapplication.cart.presentation

import com.flexeiprata.androidmytaskapplication.cart.data.ProductInCart
import io.reactivex.rxjava3.core.Observable

interface GetCartUseCase {
    operator fun invoke() : Observable<List<ProductInCart>>
}
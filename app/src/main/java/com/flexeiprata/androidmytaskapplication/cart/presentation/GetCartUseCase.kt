package com.flexeiprata.androidmytaskapplication.cart.presentation

import com.flexeiprata.androidmytaskapplication.cart.data.ProductInCart
import kotlinx.coroutines.flow.Flow

interface GetCartUseCase {
    operator fun invoke() : Flow<List<ProductInCart>>
}
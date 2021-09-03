package com.flexeiprata.androidmytaskapplication.cart.presentation

import com.flexeiprata.androidmytaskapplication.products.data.models.Product

interface AddToCartUseCase {
    suspend operator fun invoke(product: Product)
}
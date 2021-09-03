package com.flexeiprata.androidmytaskapplication.cart.domain

import com.flexeiprata.androidmytaskapplication.cart.data.ProductInCart
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCart() : Flow<List<ProductInCart>>
    suspend fun clearCart()
    suspend fun addToCart(product: Product)
}
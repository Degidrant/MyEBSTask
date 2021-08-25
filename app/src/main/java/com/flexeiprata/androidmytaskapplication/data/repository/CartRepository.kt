package com.flexeiprata.androidmytaskapplication.data.repository

import com.flexeiprata.androidmytaskapplication.data.db.CartDao
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.data.models.ProductInCart
import javax.inject.Inject

class CartRepository @Inject constructor(private val dao: CartDao) {
    fun getCart() = dao.getCart()
    suspend fun clearCart() = dao.clearCart()
    suspend fun addToCart(product: Product) {
        dao.addToCart(ProductInCart(product, 0))
    }
}
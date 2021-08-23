package com.flexeiprata.androidmytaskapplication.data.repository

import com.flexeiprata.androidmytaskapplication.data.db.CartDao
import com.flexeiprata.androidmytaskapplication.data.models.Product
import javax.inject.Inject

class CartRepository @Inject constructor(private val dao: CartDao) {
    fun getCart() = dao.getCart()
    suspend fun clearCart() = dao.clearCart()
    suspend fun addToCart(product: Product) {
        product.logicCategory = 1
        dao.addToCart(product)
    }
}
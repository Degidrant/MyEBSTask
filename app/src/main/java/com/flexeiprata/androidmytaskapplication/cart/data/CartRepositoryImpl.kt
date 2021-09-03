package com.flexeiprata.androidmytaskapplication.cart.data

import com.flexeiprata.androidmytaskapplication.cart.domain.CartRepository
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val dao: CartDao) : CartRepository{
    override fun getCart() = dao.getCart()
    override suspend fun clearCart() = dao.clearCart()

    override suspend fun addToCart(product: Product) {
        dao.addToCart(ProductInCart(product, 0))
    }
}
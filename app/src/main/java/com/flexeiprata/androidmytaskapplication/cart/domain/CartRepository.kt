package com.flexeiprata.androidmytaskapplication.cart.domain

import com.flexeiprata.androidmytaskapplication.cart.data.ProductInCart
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface CartRepository {
    fun getCart() : Observable<List<ProductInCart>>
    fun clearCart(): Completable
    fun addToCart(product: Product): Completable
}
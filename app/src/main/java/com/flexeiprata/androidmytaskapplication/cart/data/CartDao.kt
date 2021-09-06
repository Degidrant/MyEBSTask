package com.flexeiprata.androidmytaskapplication.cart.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface CartDao {

    @Query("Select * from products_in_cart")
    fun getCart(): Observable<List<ProductInCart>>

    @Query("Delete from products_in_cart")
    fun clearCart(): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addToCart(product: ProductInCart): Completable
}
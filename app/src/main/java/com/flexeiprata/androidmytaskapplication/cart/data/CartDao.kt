package com.flexeiprata.androidmytaskapplication.cart.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("Select * from products_in_cart")
    fun getCart(): Flow<List<ProductInCart>>

    @Query("Delete from products_in_cart")
    suspend fun clearCart()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(product: ProductInCart)
}
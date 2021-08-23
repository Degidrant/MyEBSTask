package com.flexeiprata.androidmytaskapplication.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flexeiprata.androidmytaskapplication.data.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("Select * from product Where logicCategory = 1")
    fun getCart(): Flow<List<Product>>

    @Query("Delete from product Where logicCategory = 1")
    suspend fun clearCart()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(product: Product)
}
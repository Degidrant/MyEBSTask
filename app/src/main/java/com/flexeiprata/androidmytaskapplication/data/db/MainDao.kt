package com.flexeiprata.androidmytaskapplication.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.flexeiprata.androidmytaskapplication.data.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface MainDao {

    @Query("Select * from product")
    fun getAllFav(): Flow<List<Product>>

    @Insert
    suspend fun insertFavorite(fav: Product)
}
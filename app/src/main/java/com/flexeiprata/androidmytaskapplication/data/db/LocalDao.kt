package com.flexeiprata.androidmytaskapplication.data.db

import android.graphics.drawable.Icon
import androidx.room.*
import com.flexeiprata.androidmytaskapplication.data.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDao {

    @Query("Select * from product")
    fun getAllFav(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(fav: Product)

    @Query("Delete from product where id = :favID")
    suspend fun deleteFavorite(favID: Int)

    @Query("Select * from product Where id = :id")
    fun getFavByID(id: Int) : Flow<Product?>

    @Query("Update product set price = :newPrice where id = :id")
    suspend fun actualizePrice(newPrice: Int, id: Int)

    @Query("Update product set name = :name, price = :price, colour = :color, size = :size, icon = :icon Where id = :id")
    suspend fun actualize(name: String, price: Int, color: String, size: String, icon: String, id: Int)

}
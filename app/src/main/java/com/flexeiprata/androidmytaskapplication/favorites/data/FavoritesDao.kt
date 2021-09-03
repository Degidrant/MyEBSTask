package com.flexeiprata.androidmytaskapplication.favorites.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

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
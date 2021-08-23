package com.flexeiprata.androidmytaskapplication.data.db

import androidx.room.*
import com.flexeiprata.androidmytaskapplication.data.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDao {

    @Query("Select * from product Where logicCategory = 0")
    fun getAllFav(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(fav: Product)

    @Query("Delete from product where id = :favID and logicCategory = 0")
    suspend fun deleteFavorite(favID: Int)

    @Query("Select * from product Where id = :id and logicCategory = 0")
    fun getFavByID(id: Int) : Flow<Product?>

    @Query("Update product set price = :newPrice Where id = :id")
    suspend fun actualizePrice(newPrice: Int, id: Int)
}
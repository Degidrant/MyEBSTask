package com.flexeiprata.androidmytaskapplication.favorites.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface FavoritesDao {

    @Query("Select * from product")
    fun getAllFavRX() : Observable<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(fav: Product) : Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteCo(fav: Product): Completable

    @Query("Delete from product where id = :favID")
    fun deleteFavorite(favID: Int): Completable

    @Query("Select * from product Where id = :id")
    fun getFavByID(id: Int) : Single<Product?>

    @Query("Update product set name = :name, price = :price, colour = :color, size = :size, icon = :icon Where id = :id")
    fun actualize(name: String, price: Int, color: String, size: String, icon: String, id: Int): Completable

}
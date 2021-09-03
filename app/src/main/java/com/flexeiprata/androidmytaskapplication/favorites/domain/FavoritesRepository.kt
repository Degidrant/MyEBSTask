package com.flexeiprata.androidmytaskapplication.favorites.domain

import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getAllFav() : Flow<List<Product>>
    suspend fun insertFav(product: Product)
    suspend fun deleteFav(id: Int)
    fun getFavByID(id: Int) : Flow<Product?>
    suspend fun actualize(product: Product)
}
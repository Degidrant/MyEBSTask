package com.flexeiprata.androidmytaskapplication.data.repository

import com.flexeiprata.androidmytaskapplication.data.db.LocalDao
import com.flexeiprata.androidmytaskapplication.data.models.Product
import javax.inject.Inject

class LocalRepository @Inject constructor(private val dao: LocalDao) {
    fun getAllFav() = dao.getAllFav()
    suspend fun insertFav(product : Product) {
        product.logicCategory = 0
        dao.insertFavorite(product)

    }
    suspend fun deleteFav(product: Product){
        dao.deleteFavorite(product.id)
    }
    fun getFavByID(id: Int) = dao.getFavByID(id)

    suspend fun actualizePrice(newPrice: Int, id: Int) = dao.actualizePrice(newPrice, id)
}
package com.flexeiprata.androidmytaskapplication.data.repository

import com.flexeiprata.androidmytaskapplication.data.db.MainDao
import com.flexeiprata.androidmytaskapplication.data.models.Product
import javax.inject.Inject

class LocalRepository @Inject constructor(private val dao: MainDao) {
    suspend fun getAllFav() = dao.getAllFav()
    suspend fun insertFav(product : Product) = dao.insertFavorite(product)
}
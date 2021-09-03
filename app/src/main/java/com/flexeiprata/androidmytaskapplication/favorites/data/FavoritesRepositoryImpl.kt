package com.flexeiprata.androidmytaskapplication.favorites.data

import com.flexeiprata.androidmytaskapplication.favorites.domain.FavoritesRepository
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(private val dao: FavoritesDao) : FavoritesRepository{
    override fun getAllFav() = dao.getAllFav()
    override suspend fun insertFav(product: Product) {
        dao.insertFavorite(product)
    }

    override suspend fun deleteFav(id: Int) {
        dao.deleteFavorite(id)
    }

    override fun getFavByID(id: Int) = dao.getFavByID(id)

    override suspend fun actualize(product: Product) = dao.actualize(product.name, product.price, product.colour, product.size, product.category.icon, product.id)
}
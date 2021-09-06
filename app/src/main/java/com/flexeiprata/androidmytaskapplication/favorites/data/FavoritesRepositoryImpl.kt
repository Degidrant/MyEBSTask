package com.flexeiprata.androidmytaskapplication.favorites.data

import com.flexeiprata.androidmytaskapplication.favorites.domain.FavoritesRepository
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(private val dao: FavoritesDao) : FavoritesRepository{
    override fun getAllFavRX() = dao.getAllFavRX()
    override fun insertFav(product: Product) = dao.insertFavorite(product)


    override fun insertFavCo(product: Product): Completable =
        dao.insertFavoriteCo(product)


    override fun deleteFav(id: Int): Completable =
        dao.deleteFavorite(id)


    override fun getFavByID(id: Int): Single<Product?> = dao.getFavByID(id)

    override fun actualize(product: Product) = dao.actualize(product.name, product.price, product.colour, product.size, product.category.icon, product.id)
}
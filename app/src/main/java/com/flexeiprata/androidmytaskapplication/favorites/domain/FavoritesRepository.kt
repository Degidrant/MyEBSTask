package com.flexeiprata.androidmytaskapplication.favorites.domain

import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface FavoritesRepository {
    fun getAllFavRX(): Observable<List<Product>>
    fun insertFav(product: Product): Completable
    fun insertFavCo(product: Product): Completable
    fun deleteFav(id: Int): Completable
    fun getFavByID(id: Int): Single<Product?>
    fun actualize(product: Product): Completable
}
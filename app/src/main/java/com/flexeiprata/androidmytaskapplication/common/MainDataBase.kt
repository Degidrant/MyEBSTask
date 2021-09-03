package com.flexeiprata.androidmytaskapplication.common

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flexeiprata.androidmytaskapplication.cart.data.CartDao
import com.flexeiprata.androidmytaskapplication.cart.data.ProductInCart
import com.flexeiprata.androidmytaskapplication.favorites.data.FavoritesDao
import com.flexeiprata.androidmytaskapplication.products.data.models.Product

@Database(entities = [Product::class, ProductInCart::class], version = 1)
abstract class MainDataBase : RoomDatabase() {
    abstract fun getMainDao(): FavoritesDao
    abstract fun getCartDao(): CartDao
}
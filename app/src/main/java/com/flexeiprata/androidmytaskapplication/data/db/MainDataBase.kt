package com.flexeiprata.androidmytaskapplication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.data.models.ProductInCart

@Database(entities = [Product::class, ProductInCart::class], version = 1)
abstract class MainDataBase : RoomDatabase() {
    abstract fun getMainDao(): LocalDao
    abstract fun getCartDao(): CartDao
}
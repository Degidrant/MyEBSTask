package com.flexeiprata.androidmytaskapplication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flexeiprata.androidmytaskapplication.data.models.Product

@Database(entities = [Product::class], version = 1)
abstract class MainDataBase : RoomDatabase() {
    abstract fun getMainDao(): MainDao
}
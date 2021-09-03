package com.flexeiprata.androidmytaskapplication.modules

import android.content.Context
import androidx.room.Room
import com.flexeiprata.androidmytaskapplication.cart.data.CartDao
import com.flexeiprata.androidmytaskapplication.common.MainDataBase
import com.flexeiprata.androidmytaskapplication.favorites.data.FavoritesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideMainDao(mainDataBase: MainDataBase): FavoritesDao {
        return mainDataBase.getMainDao()
    }

    @Provides
    fun provideCartDao(mainDataBase: MainDataBase): CartDao {
        return  mainDataBase.getCartDao()
    }

    @Provides
    @Singleton
    fun provideMainDatabase(@ApplicationContext context: Context) : MainDataBase {
        return Room.databaseBuilder(
            context,
            MainDataBase::class.java,
            "MainDatabase"
        ).build()
    }

}
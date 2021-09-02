package com.flexeiprata.androidmytaskapplication.data.modules

import android.content.Context
import androidx.room.Room
import com.flexeiprata.androidmytaskapplication.data.db.CartDao
import com.flexeiprata.androidmytaskapplication.data.db.LocalDao
import com.flexeiprata.androidmytaskapplication.data.db.MainDataBase
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
    fun provideMainDao(mainDataBase: MainDataBase): LocalDao{
        return mainDataBase.getMainDao()
    }

    @Provides
    fun provideCartDao(mainDataBase: MainDataBase): CartDao{
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

    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context) : Context = context
}
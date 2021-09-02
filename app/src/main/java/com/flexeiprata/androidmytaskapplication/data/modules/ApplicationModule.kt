package com.flexeiprata.androidmytaskapplication.data.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object ApplicationModule {
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context) : Context = context
}
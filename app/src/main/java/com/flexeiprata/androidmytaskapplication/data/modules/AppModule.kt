package com.flexeiprata.androidmytaskapplication.data.modules

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppModule : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
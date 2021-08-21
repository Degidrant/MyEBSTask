package com.flexeiprata.androidmytaskapplication.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.flexeiprata.androidmytaskapplication.data.api.ApiHelper
import com.flexeiprata.androidmytaskapplication.data.repository.MainRepository
import com.flexeiprata.androidmytaskapplication.ui.main.FavViewModel

class FavViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavViewModel::class.java))
            return FavViewModel(MainRepository(apiHelper)) as T
        else throw IllegalStateException("Unknown class name")
    }
}
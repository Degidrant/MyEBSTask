package com.flexeiprata.androidmytaskapplication.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.flexeiprata.androidmytaskapplication.data.api.ApiHelper
import com.flexeiprata.androidmytaskapplication.data.repository.MainRepository
import com.flexeiprata.androidmytaskapplication.ui.main.DescViewModel
import com.flexeiprata.androidmytaskapplication.ui.main.MainViewModel
import java.lang.IllegalStateException

class DescViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DescViewModel::class.java))
            return DescViewModel(MainRepository(apiHelper)) as T
        else throw IllegalStateException("Unknown class name")
    }
}
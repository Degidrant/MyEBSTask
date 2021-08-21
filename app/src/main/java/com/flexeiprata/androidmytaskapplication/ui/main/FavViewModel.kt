package com.flexeiprata.androidmytaskapplication.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.flexeiprata.androidmytaskapplication.data.repository.MainRepository
import com.flexeiprata.androidmytaskapplication.utils.Resource
import kotlinx.coroutines.Dispatchers

class FavViewModel(private val repository: MainRepository) : ViewModel() {
    fun getProducts() = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.getProducts(1)))
        } catch (ex: Exception) {
            emit(Resource.error(null, "Error. Exception: ${ex.message}"))
        }
    }
}
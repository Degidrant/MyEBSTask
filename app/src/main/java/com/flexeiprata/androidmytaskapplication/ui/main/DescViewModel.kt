package com.flexeiprata.androidmytaskapplication.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.flexeiprata.androidmytaskapplication.data.repository.MainRepository
import com.flexeiprata.androidmytaskapplication.utils.Resource
import kotlinx.coroutines.Dispatchers

class DescViewModel (private val repository: MainRepository) : ViewModel(){
    fun getProductsById(id : Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.getProductById(id)))
        }
        catch (ex: Exception){
            emit(Resource.error(null, "Error. Exception: ${ex.message}"))
        }
    }
}

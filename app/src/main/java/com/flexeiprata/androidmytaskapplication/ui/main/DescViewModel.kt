package com.flexeiprata.androidmytaskapplication.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.data.repository.CartRepository
import com.flexeiprata.androidmytaskapplication.data.repository.LocalRepository
import com.flexeiprata.androidmytaskapplication.data.repository.MainRepository
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.DescUIModel
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.RowDescUI
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.RowHeaderUI
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.RowMainUI
import com.flexeiprata.androidmytaskapplication.utils.LOG_DEBUG
import com.flexeiprata.androidmytaskapplication.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DescViewModel @Inject constructor(
    private val repository: MainRepository,
    private val localRepository: LocalRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    private lateinit var product: Product

    fun getProductsById(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            product = repository.getProductById(id)
            val listOfModels = mutableListOf<DescUIModel>()
            listOfModels.add(RowHeaderUI("Header", product.category.icon))
            listOfModels.add(RowMainUI("Main", product.name, String.format("%1s\n%2s", product.size, product.colour), product.price))
            listOfModels.add(RowDescUI("Desc", product.details))
            emit(Resource.success(listOfModels))
        } catch (ex: Exception) {
            emit(Resource.error(null, "Error. Exception: ${ex.message}"))
            Log.d(LOG_DEBUG, "Error. Exception: ${ex.message}")
        }
    }

    fun getFavById(id: Int) = localRepository.getFavByID(id)

    fun insertFav() = viewModelScope.launch {
        localRepository.insertFav(product)
    }

    fun deleteFav() = viewModelScope.launch {
        localRepository.deleteFav(product)
    }

    fun addToCart() = viewModelScope.launch {
        cartRepository.addToCart(product)
    }

}

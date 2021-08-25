package com.flexeiprata.androidmytaskapplication.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.data.repository.CartRepository
import com.flexeiprata.androidmytaskapplication.data.repository.LocalRepository
import com.flexeiprata.androidmytaskapplication.data.repository.MainRepository
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
    // TODO: Don't send product directly to UI. Create rows to be displayed in ViewModel and send them to UI.
    fun getProductsById(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.getProductById(id)))
        } catch (ex: Exception) {
            emit(Resource.error(null, "Error. Exception: ${ex.message}"))
        }
    }

    fun getFavById(id: Int) = localRepository.getFavByID(id)

    fun insertFav(fav: Product) = viewModelScope.launch {
        localRepository.insertFav(fav)
    }

    fun deleteFav(fav: Product) = viewModelScope.launch {
        localRepository.deleteFav(fav)
    }

    fun addToCart(product: Product) = viewModelScope.launch {
        cartRepository.addToCart(product)
    }

}

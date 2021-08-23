package com.flexeiprata.androidmytaskapplication.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.flexeiprata.androidmytaskapplication.data.api.ApiHelper
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.data.repository.CartRepository
import com.flexeiprata.androidmytaskapplication.data.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(
    private val apiHelper: ApiHelper,
    private val repository: LocalRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    fun getAllFav() = repository.getAllFav().asLiveData()


    fun insertFav(fav: Product) = viewModelScope.launch {
        repository.insertFav(fav)
    }

    fun deleteFav(fav: Product) = viewModelScope.launch {
        repository.deleteFav(fav)
    }

    fun getFavById(id: Int) = repository.getFavByID(id)

    fun getCart() = cartRepository.getCart().asLiveData()

    fun clearCart() = viewModelScope.launch {
        cartRepository.clearCart()
    }

    fun addToCart(product: Product) = viewModelScope.launch {
        cartRepository.addToCart(product)
    }

    fun actualizeData(list: List<Product>) = viewModelScope.launch {
        list.forEach {
            try {
                it.price = apiHelper.getProductById(it.id).price
                repository.actualizePrice(it.price, it.id)
            }
            catch (ex: Exception){
                ex.printStackTrace()
            }
        }
    }
}
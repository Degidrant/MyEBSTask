package com.flexeiprata.androidmytaskapplication.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.flexeiprata.androidmytaskapplication.data.api.ApiHelper
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.data.repository.CartRepository
import com.flexeiprata.androidmytaskapplication.data.repository.LocalRepository
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.ProductUIModel
import com.flexeiprata.androidmytaskapplication.utils.LOG_DEBUG
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

    /*fun actualizeData(list: List<Product>) = viewModelScope.launch {
        list.forEach {
            try {
                val price = apiHelper.getProductById(it.id).price
                if (price != it.price) repository.actualizePrice(price, it.id)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }*/

    fun actualizeData(list: List<Product>) = viewModelScope.launch {
        list.forEach {
            try {
                val product = apiHelper.getProductById(it.id)
                val comparator = ProductUIModel(product, true).isContentTheSame(ProductUIModel(it, true))
                Log.d(LOG_DEBUG, "Comparator ${product.name} = $comparator")
                if (!comparator) repository.actualize(product)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}
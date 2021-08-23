package com.flexeiprata.androidmytaskapplication.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.data.models.ProductUIModel
import com.flexeiprata.androidmytaskapplication.data.pagination.MainDataSource
import com.flexeiprata.androidmytaskapplication.data.repository.CartRepository
import com.flexeiprata.androidmytaskapplication.data.repository.LocalRepository
import com.flexeiprata.androidmytaskapplication.data.repository.MainRepository
import com.flexeiprata.androidmytaskapplication.utils.PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val localRepository: LocalRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    val listData = Pager(PagingConfig(PAGE_SIZE)) {
        MainDataSource(repository)
    }
        .liveData.cachedIn(viewModelScope).map { pagingData ->
            pagingData.map { product ->
                ProductUIModel(product)
            }
        }

    fun insertFav(fav: Product) = viewModelScope.launch {
        localRepository.insertFav(fav)
    }

    fun deleteFav(fav: Product) = viewModelScope.launch {
        localRepository.deleteFav(fav)
    }

    fun getFavById(id: Int) = localRepository.getFavByID(id)

    fun getCart() = cartRepository.getCart().asLiveData()

    fun clearCart() = viewModelScope.launch {
        cartRepository.clearCart()
    }

    fun addToCart(product: Product) = viewModelScope.launch {
        cartRepository.addToCart(product)
    }

}
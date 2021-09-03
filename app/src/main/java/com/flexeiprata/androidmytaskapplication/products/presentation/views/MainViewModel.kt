package com.flexeiprata.androidmytaskapplication.products.presentation.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.flexeiprata.androidmytaskapplication.cart.presentation.AddToCartUseCase
import com.flexeiprata.androidmytaskapplication.cart.presentation.ClearCartUseCase
import com.flexeiprata.androidmytaskapplication.cart.presentation.GetCartUseCase
import com.flexeiprata.androidmytaskapplication.common.PAGE_SIZE
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.DeleteFavUseCase
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.GetAllFavsUseCase
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.InsertFavUseCase
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import com.flexeiprata.androidmytaskapplication.products.domain.MainDataSource
import com.flexeiprata.androidmytaskapplication.products.presentation.uimodels.ProductUIModel
import com.flexeiprata.androidmytaskapplication.products.presentation.usecases.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getAllFavsUseCase: GetAllFavsUseCase,
    private val insertFavUseCase: InsertFavUseCase,
    private val deleteFavUseCase: DeleteFavUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    fun listData(text: String, listFav: List<Product>) = Pager(PagingConfig(PAGE_SIZE)) {
        MainDataSource(getProductsUseCase, text)
    }
        .flow.cachedIn(viewModelScope).map { pagingData ->
            pagingData.map { product ->
                val checker = listFav.contains(product)
                ProductUIModel(product, checker)
            }
        }

    fun getAllFav() = getAllFavsUseCase()

    fun insertFav(fav: Product) = viewModelScope.launch {
        insertFavUseCase(fav)
    }

    fun deleteFav(fav: Product) = viewModelScope.launch {
        deleteFavUseCase(fav)
    }

    fun getCart() = getCartUseCase()

    fun clearCart() = viewModelScope.launch {
        clearCartUseCase()
    }

    fun addToCart(product: Product) = viewModelScope.launch {
        addToCartUseCase(product)
    }

}
package com.flexeiprata.androidmytaskapplication.favorites.presentation.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.flexeiprata.androidmytaskapplication.cart.presentation.AddToCartUseCase
import com.flexeiprata.androidmytaskapplication.cart.presentation.ClearCartUseCase
import com.flexeiprata.androidmytaskapplication.cart.presentation.GetCartUseCase
import com.flexeiprata.androidmytaskapplication.common.LOG_DEBUG
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.*
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import com.flexeiprata.androidmytaskapplication.products.presentation.uimodels.ProductUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(
    private val getAllFavsUseCase: GetAllFavsUseCase,
    private val insertFavUseCase: InsertFavUseCase,
    private val deleteFavUseCase: DeleteFavUseCase,
    private val getFavByIdUseCase: GetFavByIdUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getProductByIdUseCase: GetFavByIdUseCase,
    private val actualizeUseCase: ActualizeUseCase
) : ViewModel() {

    private var mState = MutableStateFlow<FavResult>(FavResult.Loading(emptyList()))
    val stateInfo get() = mState.asStateFlow()
    private var mCartState = MutableStateFlow<FavResult>(FavResult.Loading(emptyList()))
    val cartStateInfo get() = mCartState.asStateFlow()
    private var listForActualization = listOf<Product>()

    fun loadAllFavs() {
        viewModelScope.launch {
            getAllFavsUseCase().collectLatest {
                mState.value = FavResult.Success(it)
                listForActualization = it
                actualizeData()
            }
        }
    }

    fun loadCart() {
        viewModelScope.launch {
            getCartUseCase().collectLatest {
                val list = emptyList<Product>().toMutableList()
                it.forEach { product ->
                    list.add(product.product)
                }
                mCartState.value = FavResult.Success(list)
            }
        }
    }

    fun insertFav(fav: Product) = viewModelScope.launch {
        insertFavUseCase(fav)
    }

    fun deleteFav(fav: Product) = viewModelScope.launch {
        deleteFavUseCase(fav.id)
    }

    fun clearCart() = viewModelScope.launch {
        clearCartUseCase()
    }

    fun addToCart(product: Product) = viewModelScope.launch {
        addToCartUseCase(product)
    }

    fun mapTheData(list: List<Product>): PagingData<ProductUIModel> = PagingData.from(list.map {
        ProductUIModel(it, true)
    })

    fun actualizeData() = viewModelScope.launch {
        listForActualization.forEach {
            try {
                val product = getProductByIdUseCase(it.id).first()!!
                val comparator =
                    ProductUIModel(product, true).isContentTheSame(ProductUIModel(it, true))
                Log.d(LOG_DEBUG, "Comparator ${product.name} = $comparator")
                if (!comparator) actualizeUseCase(product)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}
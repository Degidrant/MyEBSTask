package com.flexeiprata.androidmytaskapplication.description.presentation.views

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flexeiprata.androidmytaskapplication.cart.presentation.AddToCartUseCase
import com.flexeiprata.androidmytaskapplication.common.LOG_DEBUG
import com.flexeiprata.androidmytaskapplication.description.presentation.uiadapter.RowDescUI
import com.flexeiprata.androidmytaskapplication.description.presentation.uiadapter.RowHeaderUI
import com.flexeiprata.androidmytaskapplication.description.presentation.uiadapter.RowMainUI
import com.flexeiprata.androidmytaskapplication.description.presentation.usecases.GetProductUseCase
import com.flexeiprata.androidmytaskapplication.description.presentation.views.uimodels.RowItem
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.DeleteFavUseCase
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.GetFavByIdUseCase
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.InsertFavUseCase
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DescViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val getFavByIdUseCase: GetFavByIdUseCase,
    private val insertFavUseCase: InsertFavUseCase,
    private val deleteFavUseCase: DeleteFavUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {
    private lateinit var product: Product
    private var mutableSharedFlow =
        MutableStateFlow<DescResult>(DescResult.Loading(emptyList<RowItem>()))
    val sharedState get() = mutableSharedFlow.asStateFlow()

    fun getProductsById(id: Int) {
        viewModelScope.launch {
            try {
                product = getProductUseCase(id)
                Log.d(LOG_DEBUG, "Product: $product")
                val listOfModels = mutableListOf<RowItem>()
                listOfModels.add(RowHeaderUI("Header", product.category.icon))
                listOfModels.add(
                    RowMainUI(
                        "Main",
                        product.name,
                        String.format("%1s\n%2s", product.size, product.colour),
                        product.price
                    )
                )
                listOfModels.add(RowDescUI("Desc", product.details))
                mutableSharedFlow.value = DescResult.Success(listOfModels)
            } catch (ex: Exception) {
                mutableSharedFlow.value =
                    DescResult.Error("Error acquired during loading: ${ex.message}")
            }
        }
    }

    suspend fun checkIfIsFav(id: Int) = (getFavByIdUseCase(id).first() != null)


    fun insertFav() = viewModelScope.launch {
        try {
            insertFavUseCase(product)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun deleteFav() = viewModelScope.launch {
        try {
            deleteFavUseCase(product.id)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun addToCart() = viewModelScope.launch {
        try {
            addToCartUseCase(product)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}


package com.flexeiprata.androidmytaskapplication.description.presentation.views

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.flexeiprata.androidmytaskapplication.cart.presentation.AddToCartUseCase
import com.flexeiprata.androidmytaskapplication.common.LOG_DEBUG
import com.flexeiprata.androidmytaskapplication.common.Resource
import com.flexeiprata.androidmytaskapplication.description.presentation.uiadapter.RowDescUI
import com.flexeiprata.androidmytaskapplication.description.presentation.uiadapter.RowHeaderUI
import com.flexeiprata.androidmytaskapplication.description.presentation.uiadapter.RowMainUI
import com.flexeiprata.androidmytaskapplication.description.presentation.usecases.GetProductUseCase
import com.flexeiprata.androidmytaskapplication.description.presentation.views.uimodels.DescUIModel
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.DeleteFavUseCase
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.GetFavByIdUseCase
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.InsertFavUseCase
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    fun getProductsById(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(null))
        try {
            product = getProductUseCase(id)
            val listOfModels = mutableListOf<DescUIModel>()
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
            emit(Resource.success(listOfModels))
        } catch (ex: Exception) {
            emit(Resource.error(null, "Error. Exception: ${ex.message}"))
            Log.d(LOG_DEBUG, "Error. Exception: ${ex.message}")
        }
    }

    fun getFavById(id: Int) = getFavByIdUseCase(id)

    fun insertFav() = viewModelScope.launch {
        try {
            insertFavUseCase(product)
        } catch (ex: Exception){
            ex.printStackTrace()
        }

    }

    fun deleteFav() = viewModelScope.launch {
        try {
            deleteFavUseCase(product)
        } catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    fun addToCart(toastContext: Context) = viewModelScope.launch {
        try {
            addToCartUseCase(product)
            Toast.makeText(
                toastContext,
                "Product has been successfully added to cart!",
                Toast.LENGTH_SHORT
            ).show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}

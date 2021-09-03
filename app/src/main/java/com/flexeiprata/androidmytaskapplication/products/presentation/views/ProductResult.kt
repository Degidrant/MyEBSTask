package com.flexeiprata.androidmytaskapplication.products.presentation.views

import androidx.paging.PagingData
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import com.flexeiprata.androidmytaskapplication.products.presentation.uimodels.ProductUIModel

sealed class ProductResult {
    data class Loading(val data: List<Product>, val message: String = "") : ProductResult()
    data class Success(val data: PagingData<ProductUIModel>, val message: String = "") : ProductResult()
    data class Error(val message: String, val exception: Exception? = null) : ProductResult()
}
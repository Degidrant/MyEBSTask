package com.flexeiprata.androidmytaskapplication.favorites.presentation.view

import com.flexeiprata.androidmytaskapplication.products.data.models.Product

sealed class FavResult {
    data class Loading(val data: List<Product>, val message: String = "") : FavResult()
    data class Success(val data: List<Product>, val message: String = "") : FavResult()
    data class Error(val message: String, val exception: Exception? = null) : FavResult()
}
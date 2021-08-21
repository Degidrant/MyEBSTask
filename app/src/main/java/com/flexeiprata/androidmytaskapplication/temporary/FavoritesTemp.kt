package com.flexeiprata.androidmytaskapplication.temporary

import androidx.lifecycle.MutableLiveData
import com.flexeiprata.androidmytaskapplication.data.models.Product

object FavoritesTemp {
        val favoriteList = mutableListOf<Product>()
        val cart = mutableListOf<Product>()
        val cartObserver = MutableLiveData(cart)
        val favObserver = MutableLiveData(favoriteList)
}
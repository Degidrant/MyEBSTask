package com.flexeiprata.androidmytaskapplication.products.presentation.uimodels

import com.flexeiprata.androidmytaskapplication.common.Payloadable

sealed class ProductPayloads : Payloadable {
    data class PriceChanged(val price: Int) : ProductPayloads()
    data class NameChanged(val name: String): ProductPayloads()
    data class PicChanged(val url: String) : ProductPayloads()
    data class DescChanged(val color: String, val size: String): ProductPayloads()
    data class FavChanged(val isFav: Boolean) : ProductPayloads()
}
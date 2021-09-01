package com.flexeiprata.androidmytaskapplication.ui.models.payloads

import com.flexeiprata.androidmytaskapplication.ui.common.Payloadable

sealed class ProductPayloads : Payloadable {
    data class PriceChanged(val price: Int) : ProductPayloads()
    data class NameChanged(val name: String): ProductPayloads()
    data class PicChanged(val url: String) : ProductPayloads()
    data class DescChanged(val color: String, val size: String): ProductPayloads()
    data class FavChanged(val isFav: Boolean) : ProductPayloads()
}
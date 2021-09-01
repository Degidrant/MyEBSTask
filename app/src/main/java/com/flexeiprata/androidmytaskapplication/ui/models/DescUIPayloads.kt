package com.flexeiprata.androidmytaskapplication.ui.models

import com.flexeiprata.androidmytaskapplication.ui.common.Payloadable

sealed class DescUIPayloads : Payloadable {
    data class ImageChanged(val image: String) : DescUIPayloads()
    data class TitleChanged(val title: String): DescUIPayloads()
    data class DescChanged(val shortDesc: String) : DescUIPayloads()
    data class PriceChanged(val price: Int) : DescUIPayloads()
    data class LongDescChanged(val desc: String): DescUIPayloads()
}
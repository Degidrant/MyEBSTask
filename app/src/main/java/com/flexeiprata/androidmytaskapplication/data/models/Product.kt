package com.flexeiprata.androidmytaskapplication.data.models

import com.google.gson.annotations.SerializedName

data class Product(
    val category: Category,
    val colour: String,
    val details: String,
    val id: Int,
    val name: String,
    val price: Int,
    val size: String,
    @SerializedName("sold_count")
    val soldCount: Int
)
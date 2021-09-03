package com.flexeiprata.androidmytaskapplication.products.data.models

import com.google.gson.annotations.SerializedName

data class Category(
    val icon: String,
    @SerializedName("id")
    val catId: Int,
    @SerializedName("name")
    val catName: String
)
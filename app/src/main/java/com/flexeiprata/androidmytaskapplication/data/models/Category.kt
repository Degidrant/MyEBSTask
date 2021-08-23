package com.flexeiprata.androidmytaskapplication.data.models

import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

data class Category(
    val icon: String,
    @SerializedName("id")
    val catId: Int,
    @SerializedName("name")
    val catName: String
)
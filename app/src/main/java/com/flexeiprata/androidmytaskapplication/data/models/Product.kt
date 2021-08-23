package com.flexeiprata.androidmytaskapplication.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
    data class Product(
    @Embedded val category: Category,
    val colour: String,
    val details: String,
    val id: Int,
    val name: String,
    val price: Int,
    val size: String,
    @SerializedName("sold_count") @ColumnInfo(name = "sold_count")
    val soldCount: Int,
    var logicCategory: Int = 0,
    @PrimaryKey(autoGenerate = true) val roomId: Int
)
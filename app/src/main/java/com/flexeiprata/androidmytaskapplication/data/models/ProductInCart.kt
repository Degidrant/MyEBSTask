package com.flexeiprata.androidmytaskapplication.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products_in_cart")
data class ProductInCart(
    @Embedded val product: Product,
    @PrimaryKey(autoGenerate = true) val cartID: Int
)
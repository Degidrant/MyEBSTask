package com.flexeiprata.androidmytaskapplication.cart.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flexeiprata.androidmytaskapplication.products.data.models.Product

@Entity(tableName = "products_in_cart")
data class ProductInCart(
    @Embedded val product: Product,
    @PrimaryKey(autoGenerate = true) val cartID: Int
)
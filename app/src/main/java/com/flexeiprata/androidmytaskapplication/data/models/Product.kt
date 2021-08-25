package com.flexeiprata.androidmytaskapplication.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.flexeiprata.androidmytaskapplication.ui.common.Payloadable
import com.google.gson.annotations.SerializedName

@Entity
data class Product(
    @Embedded val category: Category,
    val colour: String,
    val details: String,
    val id: Int,
    val name: String,
    var price: Int,
    val size: String,
    @SerializedName("sold_count") @ColumnInfo(name = "sold_count")
    val soldCount: Int,
    @PrimaryKey(autoGenerate = true) val roomId: Int
){
    override fun equals(other: Any?): Boolean {
        return if (other is Product) id == other.id
        else
            super.equals(other)
    }
}

@Entity(tableName = "products_in_cart")
data class ProductInCart(
    @Embedded val product: Product,
    @PrimaryKey(autoGenerate = true) val cartID: Int
)

data class ProductUIModel(val product: Product, var isFav: Boolean) : Item {
    override fun isItemTheSame(other: Item): Boolean {
        return if (other is ProductUIModel) {
            product.id == other.product.id
        } else
            false
    }

    override fun isContentTheSame(other: Item): Boolean {
        return if (other is ProductUIModel)
            product.price == other.product.price &&
                    product.name == other.product.name &&
                    product.colour == other.product.colour &&
                    product.size == other.product.size &&
                    product.category.icon == other.product.category.icon &&
                    isFav == isFav
        else false
    }

    override fun payloads(other: Item): List<Payloadable> {
        val payloads = mutableListOf<Payloadable>()
        if (other is ProductUIModel) {
            if (product.price != other.product.price)
                payloads.add(ProductPayloads.PriceChanged(other.product.price))
            if (product.name != other.product.name)
                payloads.add(ProductPayloads.NameChanged(other.product.name))
            if (product.category.icon != other.product.category.icon)
                payloads.add(ProductPayloads.PicChanged(other.product.category.icon))
            if (product.size != other.product.size || product.colour != other.product.colour)
                payloads.add(ProductPayloads.DescChanged(other.product.colour, other.product.size))
            if (isFav != other.isFav)
                payloads.add(ProductPayloads.FavChanged(other.isFav))
        }
        return payloads
    }

}
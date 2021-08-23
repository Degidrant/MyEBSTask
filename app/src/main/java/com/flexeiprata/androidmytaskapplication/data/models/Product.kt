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
    var logicCategory: Int = 0,
    @PrimaryKey(autoGenerate = true) val roomId: Int
)

data class ProductUIModel(val product: Product) : Item {
    override fun isItemTheSame(other: Item): Boolean {
        return if (other is ProductUIModel) {
            product.id == other.product.id
        } else
            false
    }

    override fun isContentTheSame(other: Item): Boolean {
        return if (other is ProductUIModel) {
            product.price == other.product.price &&
                    product.name == other.product.name &&
                    product.colour == other.product.colour &&
                    product.size == other.product.size &&
                    product.category.icon == other.product.category.icon
        } else {
            false
        }
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
        }
        return payloads
    }

}
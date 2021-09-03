package com.flexeiprata.androidmytaskapplication.products.presentation.uimodels

import com.flexeiprata.androidmytaskapplication.common.Payloadable
import com.flexeiprata.androidmytaskapplication.products.data.models.Item
import com.flexeiprata.androidmytaskapplication.products.data.models.Product

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
                    isFav == other.isFav
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
package com.flexeiprata.androidmytaskapplication.ui.models

import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.ui.common.Payloadable

data class RowHeaderUI(val tag : String, val image: String) : DescUIModel() {
    override fun id(): Any = tag
    override fun equality(other: DescUIModel): Boolean = equals(other)
    override fun payloads(other: DescUIModel): List<Payloadable> {
        val payloads = mutableListOf<Payloadable>()
        if (other is RowHeaderUI && other.image != image) payloads.add(DescUIPayloads.ImageChanged(other.image))
        return payloads
    }

}

data class RowMainUI(val tag: String, val name: String, val shortDesc: String, val price: Int): DescUIModel() {
    override fun id(): Any = tag
    override fun equality(other: DescUIModel): Boolean = equals(other)
    override fun payloads(other: DescUIModel): List<Payloadable> {
        val payloads = mutableListOf<Payloadable>()
        if (other is RowMainUI && other.name != name) payloads.add(DescUIPayloads.TitleChanged(other.name))
        if (other is RowMainUI && other.price != price) payloads.add(DescUIPayloads.PriceChanged(other.price))
        if (other is RowMainUI && other.shortDesc != shortDesc) payloads.add(DescUIPayloads.DescChanged(other.shortDesc))
        return payloads
    }
}

data class RowDescUI(val tag: String, val desc: String) : DescUIModel() {
    override fun id(): Any = tag
    override fun equality(other: DescUIModel): Boolean = equals(other)
    override fun payloads(other: DescUIModel): List<Payloadable> {
        val payloads = mutableListOf<Payloadable>()
        if (other is RowDescUI && other.desc != desc) payloads.add(DescUIPayloads.LongDescChanged(other.desc))
        return payloads
    }
}


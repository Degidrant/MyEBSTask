package com.flexeiprata.androidmytaskapplication.description.presentation.uiadapter

import com.flexeiprata.androidmytaskapplication.common.Payloadable
import com.flexeiprata.androidmytaskapplication.description.presentation.views.uimodels.DescUIPayloads
import com.flexeiprata.androidmytaskapplication.description.presentation.views.uimodels.RowItem

data class RowHeaderUI(val tag : String, val image: String) : RowItem() {
    override fun id(): Any = tag
    override fun equality(other: RowItem): Boolean = equals(other)
    override fun payloads(other: RowItem): List<Payloadable> {
        val payloads = mutableListOf<Payloadable>()
        if (other is RowHeaderUI && other.image != image) payloads.add(DescUIPayloads.ImageChanged(other.image))
        return payloads
    }

}

data class RowMainUI(val tag: String, val name: String, val shortDesc: String, val price: Int): RowItem() {
    override fun id(): Any = tag
    override fun equality(other: RowItem): Boolean = equals(other)
    override fun payloads(other: RowItem): List<Payloadable> {
        val payloads = mutableListOf<Payloadable>()
        if (other is RowMainUI && other.name != name) payloads.add(DescUIPayloads.TitleChanged(other.name))
        if (other is RowMainUI && other.price != price) payloads.add(DescUIPayloads.PriceChanged(other.price))
        if (other is RowMainUI && other.shortDesc != shortDesc) payloads.add(DescUIPayloads.DescChanged(other.shortDesc))
        return payloads
    }
}

data class RowDescUI(val tag: String, val desc: String) : RowItem() {
    override fun id(): Any = tag
    override fun equality(other: RowItem): Boolean = equals(other)
    override fun payloads(other: RowItem): List<Payloadable> {
        val payloads = mutableListOf<Payloadable>()
        if (other is RowDescUI && other.desc != desc) payloads.add(DescUIPayloads.LongDescChanged(other.desc))
        return payloads
    }
}


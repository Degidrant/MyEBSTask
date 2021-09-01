package com.flexeiprata.androidmytaskapplication.ui.models

import com.flexeiprata.androidmytaskapplication.data.models.Product

data class RowHeaderUI(val tag : String, val image: String) : DescUIModel() {
    override fun id(): Any = tag
    override fun equality(other: DescUIModel): Boolean = equals(other)

}

data class RowMainUI(val tag: String, val name: String, val shortDesc: String, val price: Int): DescUIModel() {
    override fun id(): Any = tag
    override fun equality(other: DescUIModel): Boolean = equals(other)
}

data class RowDescUI(val tag: String, val desc: String) : DescUIModel() {
    override fun id(): Any = tag
    override fun equality(other: DescUIModel): Boolean = equals(other)
}


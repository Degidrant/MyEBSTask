package com.flexeiprata.androidmytaskapplication.ui.models

data class RowHeaderUI(val image: String) : DescUIModel()
data class RowMainUI(val name: String, val shortDesc: String, val price: Int): DescUIModel()
data class RowDescUI(val desc: String) : DescUIModel()

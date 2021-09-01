package com.flexeiprata.androidmytaskapplication.ui.models

abstract class DescUIModel(){
    abstract fun id() : Any
    abstract fun equality(other: DescUIModel) : Boolean
}
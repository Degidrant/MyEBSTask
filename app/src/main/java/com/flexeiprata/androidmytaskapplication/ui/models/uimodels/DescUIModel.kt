package com.flexeiprata.androidmytaskapplication.ui.models.uimodels

import com.flexeiprata.androidmytaskapplication.ui.common.Payloadable

abstract class DescUIModel(){
    abstract fun id() : Any
    abstract fun equality(other: DescUIModel) : Boolean
    abstract fun payloads(other: DescUIModel) : List<Payloadable>
}
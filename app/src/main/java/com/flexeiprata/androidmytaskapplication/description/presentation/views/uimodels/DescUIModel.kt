package com.flexeiprata.androidmytaskapplication.description.presentation.views.uimodels

import com.flexeiprata.androidmytaskapplication.common.Payloadable

abstract class DescUIModel(){
    abstract fun id() : Any
    abstract fun equality(other: DescUIModel) : Boolean
    abstract fun payloads(other: DescUIModel) : List<Payloadable>
}
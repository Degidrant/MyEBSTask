package com.flexeiprata.androidmytaskapplication.description.presentation.views.uimodels

import com.flexeiprata.androidmytaskapplication.common.Payloadable

abstract class RowItem(){
    abstract fun id() : Any
    abstract fun equality(other: RowItem) : Boolean
    abstract fun payloads(other: RowItem) : List<Payloadable>
}
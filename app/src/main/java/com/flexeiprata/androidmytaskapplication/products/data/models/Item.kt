package com.flexeiprata.androidmytaskapplication.products.data.models

import com.flexeiprata.androidmytaskapplication.common.Payloadable

interface Item {
    fun isItemTheSame(other: Item): Boolean
    fun isContentTheSame(other: Item): Boolean
    fun payloads(other: Item): List<Payloadable>
}
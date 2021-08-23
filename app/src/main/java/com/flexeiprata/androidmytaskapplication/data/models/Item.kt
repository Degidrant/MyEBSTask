package com.flexeiprata.androidmytaskapplication.data.models

import com.flexeiprata.androidmytaskapplication.ui.common.Payloadable

interface Item {
    fun isItemTheSame(other: Item): Boolean
    fun isContentTheSame(other: Item): Boolean
    fun payloads(other: Item): List<Payloadable>
}
package com.flexeiprata.androidmytaskapplication.description.presentation.uiadapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class DescAbstractVH(view: View) : RecyclerView.ViewHolder(view) {
    open fun <T> bind(item: T) {}
    open fun bind() {}
    open fun updateImage(image: String) {}
    open fun updateTitle(title: String) {}
    open fun updateDesc(desc: String) {}
    open fun updateLongDesc(desc: String) {}
    open fun updatePrice(price: Int) {}

}
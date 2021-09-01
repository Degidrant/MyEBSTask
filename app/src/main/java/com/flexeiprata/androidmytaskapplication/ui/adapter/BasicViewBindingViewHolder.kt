package com.flexeiprata.androidmytaskapplication.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BasicViewBindingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun <T> bind(item: T) {}
    open fun bind() {}
    open fun updateImage(image: String) {}
    open fun updateTitle(title: String) {}
    open fun updateDesc(desc: String) {}
    open fun updateLongDesc(desc: String) {}
    open fun updatePrice(price: Int) {}

}
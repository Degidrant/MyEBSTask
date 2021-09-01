package com.flexeiprata.androidmytaskapplication.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BasicViewBindingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun <T> bind(item: T) {}
    open fun bind() {}
}
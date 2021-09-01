package com.flexeiprata.androidmytaskapplication.ui.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.ProductUIModel

abstract class ProductAbstractVH(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(item: ProductUIModel)
    abstract fun updatePrice(price: Int)
    abstract fun updateName(name: String)
    abstract fun updateImage(url: String)
    abstract fun updateDesc(desc: String)
    abstract fun updateButton(fav: Boolean)
}
package com.flexeiprata.androidmytaskapplication.ui.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentBody2Binding
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentBodyBinding
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentHeaderBinding
import com.flexeiprata.androidmytaskapplication.ui.models.RowDescUI
import com.flexeiprata.androidmytaskapplication.ui.models.RowHeaderUI
import com.flexeiprata.androidmytaskapplication.ui.models.RowMainUI

class HeaderViewHolder(private val view: DescFragmentHeaderBinding) :
    BasicViewBindingViewHolder(view.root) {
    override fun <T> bind(item: T) {
        if (item is RowHeaderUI) {
            Glide.with(view.mainPhotoImage.context)
                .load(item.image)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(view.mainPhotoImage)
        }
    }
}

class MainContentViewHolder(private val view: DescFragmentBodyBinding) :
    BasicViewBindingViewHolder(view.root) {
    override fun <T> bind(item: T) {
        if (item is RowMainUI) {
            view.apply {
                textViewName.text = item.name
                textViewDesc.text = item.shortDesc
                val priceText = String.format("$${item.price}.-")
                textViewPrice.text = priceText
                textViewPriceSmall.text = priceText
            }
        }
    }
}

class DescriptionViewHolder(private val view: DescFragmentBody2Binding) :
    BasicViewBindingViewHolder(view.root) {
    override fun <T> bind(item: T) {
        if (item is RowDescUI) {
            view.apply {
                textViewFullDesc.text = item.desc
            }
        }
    }
}
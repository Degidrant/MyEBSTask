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
            updateImage(item.image)
        }
    }

    override fun updateImage(image: String) {
        Glide.with(view.mainPhotoImage.context)
            .load(image)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(view.mainPhotoImage)
    }
}

class MainContentViewHolder(private val view: DescFragmentBodyBinding) :
    BasicViewBindingViewHolder(view.root) {
    override fun <T> bind(item: T) {
        if (item is RowMainUI) {
            view.apply {
                updateTitle(item.name)
                updateDesc(item.shortDesc)
                updatePrice(item.price)
            }
        }
    }

    override fun updateTitle(title: String) {
        view.apply {
            textViewName.text = title
        }
    }

    override fun updateDesc(desc: String) {
        view.apply {
            textViewDesc.text = desc
        }
    }

    override fun updatePrice(price: Int) {
        val priceText = String.format("$${price}.-")
        view.apply {
            textViewPrice.text = priceText
            textViewPriceSmall.text = priceText
        }
    }
}

class DescriptionViewHolder(private val view: DescFragmentBody2Binding) :
    BasicViewBindingViewHolder(view.root) {
    override fun <T> bind(item: T) {
        if (item is RowDescUI) {
            view.apply {
                updateLongDesc(item.desc)
            }
        }
    }

    override fun updateLongDesc(desc: String) {
        view.apply {
            textViewFullDesc.text = desc
        }
    }
}
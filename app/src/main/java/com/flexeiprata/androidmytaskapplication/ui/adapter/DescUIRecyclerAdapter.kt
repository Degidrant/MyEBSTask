package com.flexeiprata.androidmytaskapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentBody2Binding
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentBodyBinding
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentHeaderBinding
import com.flexeiprata.androidmytaskapplication.ui.models.DescUIModel
import com.flexeiprata.androidmytaskapplication.ui.models.RowDescUI
import com.flexeiprata.androidmytaskapplication.ui.models.RowHeaderUI
import com.flexeiprata.androidmytaskapplication.ui.models.RowMainUI

//  TODO: Also, implement DiffUtil for this page. To make it easier, you can use ListAdapter instead of RecyclerView.Adapter
class DescUIRecyclerAdapter(
    private val listOfModels: MutableList<DescUIModel>
) :
    RecyclerView.Adapter<BasicViewBindingViewHolder>() {

    companion object {
        const val HEADER = 0
        const val BODY_MAIN = 1
        const val BODY_DESC = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when(listOfModels[position]) {
            is RowHeaderUI -> HEADER
            is RowMainUI -> BODY_MAIN
            is RowDescUI -> BODY_DESC
            else -> throw Exception("Invalid viewHolder exception")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BasicViewBindingViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return when (viewType){
                HEADER -> HeaderViewHolder(DescFragmentHeaderBinding.inflate(inflater, parent, false))
                BODY_MAIN -> MainContentViewHolder(DescFragmentBodyBinding.inflate(inflater, parent, false))
                BODY_DESC -> DescriptionViewHolder(DescFragmentBody2Binding.inflate(inflater, parent, false))
                else -> throw Exception("onCreateViewHolder inflate exception")
            }
        }


    override fun onBindViewHolder(holder: BasicViewBindingViewHolder, position: Int) {
        holder.bind(listOfModels[position])
    }

    override fun getItemCount(): Int = listOfModels.size

    inner class HeaderViewHolder(private val view: DescFragmentHeaderBinding) :
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

    inner class MainContentViewHolder(private val view: DescFragmentBodyBinding) :
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

    inner class DescriptionViewHolder(private val view: DescFragmentBody2Binding) :
        BasicViewBindingViewHolder(view.root) {
        override fun <T> bind(item: T) {
            if (item is RowDescUI) {
                view.apply {
                    textViewFullDesc.text = item.desc
                }
            }
        }
    }

}
package com.flexeiprata.androidmytaskapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentBody2Binding
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentBodyBinding
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentHeaderBinding

class DescUIRecyclerAdapter(
    private val product: Product,
    private val controller: Fragment,
) :
    RecyclerView.Adapter<DescUIRecyclerAdapter.BasicViewBindingViewHolder>() {

    companion object {
        const val HEADER = 1
        const val BODY_MAIN = 2
        const val BODY_DESC = 3

        const val SIZE = 3
    }

    override fun getItemViewType(position: Int): Int {
        return (position + 1)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BasicViewBindingViewHolder {
        val binding: ViewBinding
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HEADER -> {
                binding = DescFragmentHeaderBinding.inflate(inflater, parent, false)
                HeaderDescViewHolder(binding)
            }
            BODY_MAIN -> {
                binding = DescFragmentBodyBinding.inflate(inflater, parent, false)
                MainBodyDescViewHolder(binding)
            }
            BODY_DESC -> {
                binding = DescFragmentBody2Binding.inflate(inflater, parent, false)
                SecondBodyDescViewHolder(binding)
            }
            else -> throw IllegalStateException("Invalid structure parameter")
        }
    }

    override fun onBindViewHolder(holder: BasicViewBindingViewHolder, position: Int) {
        holder.bind(product)
    }

    override fun getItemCount(): Int = SIZE

    abstract class BasicViewBindingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        open fun <T> bind(item: T) {}
        open fun bind() {}
    }

    inner class HeaderDescViewHolder(private val view: DescFragmentHeaderBinding) :
        BasicViewBindingViewHolder(view.root) {
        override fun <T> bind(item: T) {
            if (item is Product) {
                Glide.with(view.mainPhotoImage.context)
                    .load(product.category.icon)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(view.mainPhotoImage)
            }
        }
    }

    inner class MainBodyDescViewHolder(private val view: DescFragmentBodyBinding) :
        BasicViewBindingViewHolder(view.root) {
        override fun <T> bind(item: T) {
            if (item is Product) {
                view.apply {
                    textViewName.text = item.name
                    textViewDesc.text = String.format("%1s\n%2s", item.size, item.colour)
                    val priceText = String.format("$${item.price}.-")
                    textViewPrice.text = priceText
                    textViewPriceSmall.text = priceText
                }
            }
        }
    }

    inner class SecondBodyDescViewHolder(private val view: DescFragmentBody2Binding) :
        BasicViewBindingViewHolder(view.root) {
        override fun <T> bind(item: T) {
            if (item is Product) {
                view.apply {
                    textViewFullDesc.text = item.details
                }
            }
        }
    }

}
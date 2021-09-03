package com.flexeiprata.androidmytaskapplication.products.presentation.adapter

import android.content.res.ColorStateList
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.databinding.MainAdapterGridBinding
import com.flexeiprata.androidmytaskapplication.products.presentation.uimodels.ProductUIModel

class ProductGridViewHolder(private val view: MainAdapterGridBinding, private val parentInterface: ProductsAdapter.FavoriteSwitch) :
    ProductAbstractVH(view.root) {
    override fun bind(item: ProductUIModel) {
        val product = item.product
        view.apply {

            updatePrice(product.price)
            updateName(product.name)
            updateDesc(String.format("%1s\n%2s", product.size, product.colour))
            updateImage(product.category.icon)
            updateButton(item.isFav)

            itemView.setOnClickListener {
                parentInterface.navigateToNext(product.id)
            }

            fabInCart.setOnClickListener {
                parentInterface.addToCart(product)
                Toast.makeText(
                    view.root.context,
                    "${product.name} has been added to cart!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            fabFavorite.setOnClickListener {
                with(!item.isFav) {
                    if (this) parentInterface.insertFav(product)
                    else parentInterface.deleteFav(product)
                    updateButton(this)
                    item.isFav = !item.isFav
                }
            }
        }
    }

    override fun updatePrice(price: Int) {
        view.apply {
            val pricePlace = String.format("$%d,-", price)
            textViewPrice.text = pricePlace
            textViewPriceSmall.text = pricePlace
        }
    }

    override fun updateName(name: String) {
        view.apply {
            textViewName.text = name
        }
    }

    override fun updateImage(url: String) {
        view.imageView.apply {
            Glide.with(this.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(this)
        }

    }

    override fun updateDesc(desc: String) {
        view.apply {
            textViewDesc.text = desc
        }
    }

    override fun updateButton(fav: Boolean) {
        // FABs background color cannot be accessed together with image drawable
        view.apply {
            if (fav) {
                fabFavorite.apply {
                    setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ns_favorite_full))
                    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.warm_yellow))
                }
            } else {
                fabFavorite.apply {
                    setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ns_like))
                    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
                }
            }
        }
    }
}
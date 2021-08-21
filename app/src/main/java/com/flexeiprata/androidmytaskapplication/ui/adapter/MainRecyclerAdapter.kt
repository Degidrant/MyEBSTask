package com.flexeiprata.androidmytaskapplication.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.databinding.MainAdapterBinding
import com.flexeiprata.androidmytaskapplication.databinding.MainAdapterGridBinding
import com.flexeiprata.androidmytaskapplication.temporary.FavoritesTemp
import com.flexeiprata.androidmytaskapplication.ui.fragment.MainFragmentDirections
import com.flexeiprata.androidmytaskapplication.utils.LOG_DEBUG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainRecyclerAdapter(
    val coroutineScope: CoroutineScope,
    val navController: NavController,
    var thisLayoutManager: GridLayoutManager
) : PagingDataAdapter<Product, MainRecyclerAdapter.ViewBindingViewHolder>(MainDiffUtil) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerAdapter.ViewBindingViewHolder {
        context = parent.context
        return if (viewType == 1) {
            val binding =
                MainAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            Log.d(LOG_DEBUG, "Linear!")
            InnerViewHolder(binding)
        } else {
            val binding =
                MainAdapterGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            Log.d(LOG_DEBUG, "Grid!")
            InnerGridViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: ViewBindingViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }

    }

    open inner class ViewBindingViewHolder(view: View) : RecyclerView.ViewHolder(view){
        open fun bind(product: Product){

        }
    }

    object MainDiffUtil : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (thisLayoutManager.spanCount == 1) 1 else 2
    }

    inner class InnerViewHolder(private val view: MainAdapterBinding) :
        ViewBindingViewHolder(view.root) {
        override fun bind(product: Product) {
            view.apply {
                textViewName.text = product.name
                textViewDesc.text = String.format("%1s\n%2s", product.size, product.colour)
                val pricePlace = String.format("$%d,-", product.price)
                textViewPrice.text = pricePlace
                textViewPriceSmall.text = pricePlace

                coroutineScope.launch(Dispatchers.IO) {
                    val glide = Glide.with(imageView.context)
                        .load(product.category.icon)
                    withContext(Dispatchers.Main) {
                        glide.into(imageView)
                    }
                }

                setButtonStyle(FavoritesTemp.favoriteList.contains(product))

                itemView.setOnClickListener {
                    navController.navigate(
                        MainFragmentDirections.actionMainFragmentToDescFragment(
                            product.id,
                            FavoritesTemp.favoriteList.contains(product)
                        )
                    )
                }

                fabInCart.setOnClickListener {
                    FavoritesTemp.cart.add(product)
                    FavoritesTemp.cartObserver.postValue(FavoritesTemp.cart)
                }

                fabFavorite.setOnClickListener {
                    with(!FavoritesTemp.favoriteList.contains(product)) {
                        setButtonStyle(this)
                        if (this) FavoritesTemp.favoriteList.add(product)
                        else FavoritesTemp.favoriteList.remove(product)
                        FavoritesTemp.favObserver.value = FavoritesTemp.favoriteList
                    }
                }
            }
        }

        private fun setButtonStyle(setter: Boolean) {
            view.apply {
                if (setter) {
                    fabFavorite.apply {
                        setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ns_favorite_full
                            )
                        )
                        imageTintList = ColorStateList.valueOf(context.getColor(R.color.funny_orange))
                        backgroundTintList =
                            ColorStateList.valueOf(context.getColor(R.color.warm_yellow))
                    }
                } else {
                    fabFavorite.apply {
                        setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ns_like
                            )
                        )
                        imageTintList = ColorStateList.valueOf(context.getColor(R.color.funny_orange))
                        backgroundTintList =
                            ColorStateList.valueOf(context.getColor(R.color.white))
                    }
                }
            }
        }
    }

    inner class InnerGridViewHolder(private val view: MainAdapterGridBinding) :
        ViewBindingViewHolder(view.root) {
        override fun bind(product: Product) {
            view.apply {
                textViewName.text = product.name
                textViewDesc.text = String.format("%1s\n%2s", product.size, product.colour)
                val pricePlace = String.format("$%d,-", product.price)
                textViewPrice.text = pricePlace
                textViewPriceSmall.text = pricePlace

                coroutineScope.launch(Dispatchers.IO) {
                    val glide = Glide.with(imageView.context)
                        .load(product.category.icon)
                    withContext(Dispatchers.Main) {
                        glide.into(imageView)
                        //imageView.startAnimation(getFadeInAnimation(500))
                    }
                }

                setButtonStyle(FavoritesTemp.favoriteList.contains(product))

                itemView.setOnClickListener {
                    navController.navigate(
                        MainFragmentDirections.actionMainFragmentToDescFragment(
                            product.id,
                            FavoritesTemp.favoriteList.contains(product)
                        )
                    )
                }

                fabInCart.setOnClickListener {
                    FavoritesTemp.cart.add(product)
                    FavoritesTemp.cartObserver.postValue(FavoritesTemp.cart)
                }

                fabFavorite.setOnClickListener {
                    with(!FavoritesTemp.favoriteList.contains(product)) {
                        setButtonStyle(this)
                        if (this) FavoritesTemp.favoriteList.add(product)
                        else FavoritesTemp.favoriteList.remove(product)
                        FavoritesTemp.favObserver.value = FavoritesTemp.favoriteList
                    }
                }
            }
        }

        private fun setButtonStyle(setter: Boolean) {
            view.apply {
                if (setter) {
                    fabFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_favorite
                        )
                    )
                    fabFavorite.backgroundTintList =
                        ColorStateList.valueOf(context.getColor(R.color.mild_orange))
                } else {
                    fabFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_heart
                        )
                    )
                    fabFavorite.backgroundTintList =
                        ColorStateList.valueOf(context.getColor(R.color.white))
                }
            }
        }
    }


}

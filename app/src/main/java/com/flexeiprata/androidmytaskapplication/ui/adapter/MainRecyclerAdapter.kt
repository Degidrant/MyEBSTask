package com.flexeiprata.androidmytaskapplication.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.databinding.MainAdapterBinding
import com.flexeiprata.androidmytaskapplication.databinding.MainAdapterGridBinding
import com.flexeiprata.androidmytaskapplication.ui.fragment.MainFragmentDirections
import com.flexeiprata.androidmytaskapplication.utils.LOG_DEBUG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainRecyclerAdapter(
    val parentFragment: Fragment,
    val navController: NavController,
    var thisLayoutManager: GridLayoutManager
) : PagingDataAdapter<Product, MainRecyclerAdapter.ViewBindingViewHolder>(MainDiffUtil) {

    private lateinit var context: Context
    private lateinit var parentInterface: FavoriteSwitch

    interface FavoriteSwitch{
        fun deleteFav(fav: Product)
        fun insertFav(fav: Product)
        fun getFavByID(id: Int) : Flow<Product?>
        fun addToCart(product: Product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerAdapter.ViewBindingViewHolder {
        context = parent.context
        parentInterface = parentFragment as FavoriteSwitch
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

    abstract class ViewBindingViewHolder(view: View) : RecyclerView.ViewHolder(view){
        abstract fun bind(product: Product)
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

                parentFragment.lifecycleScope.launch(Dispatchers.IO) {
                    val glide = Glide.with(imageView.context)
                        .load(product.category.icon)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    withContext(Dispatchers.Main) {
                        glide.into(imageView)
                    }
                }

                parentFragment.lifecycleScope.launch(Dispatchers.IO) {
                    val checker = parentInterface.getFavByID(product.id).first() == null
                    withContext(Dispatchers.Main){
                        setButtonStyle(!checker)
                    }
                }


                itemView.setOnClickListener {
                    parentFragment.lifecycleScope.launch(Dispatchers.IO){
                        val checker = parentInterface.getFavByID(product.id).first() == null
                        withContext(Dispatchers.Main) {
                            navController.navigate(
                                MainFragmentDirections.actionMainFragmentToDescFragment(
                                    product.id,
                                    !checker
                                )
                            )
                        }
                    }
                }

                fabInCart.setOnClickListener {
                    parentInterface.addToCart(product)
                    Toast.makeText(context, "${product.name} has been added to cart!", Toast.LENGTH_SHORT).show()
                }

                fabFavorite.setOnClickListener {
                    parentFragment.lifecycleScope.launch(Dispatchers.IO) {
                        Log.d(LOG_DEBUG, parentInterface.getFavByID(product.id).first()?.toString() ?: "Not found with ${product.id}")
                        with(parentInterface.getFavByID(product.id).first() == null) {
                            if (this) parentInterface.insertFav(product)
                            else parentInterface.deleteFav(product)
                            withContext(Dispatchers.Main){
                                setButtonStyle(this@with)
                            }
                        }
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

                parentFragment.lifecycleScope.launch(Dispatchers.IO) {
                    val glide = Glide.with(imageView.context)
                        .load(product.category.icon)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    withContext(Dispatchers.Main) {
                        glide.into(imageView)
                    }
                }

                parentFragment.lifecycleScope.launch(Dispatchers.IO) {
                    val checker = parentInterface.getFavByID(product.id).first() == null
                    withContext(Dispatchers.Main){
                        setButtonStyle(!checker)
                    }
                }

                itemView.setOnClickListener {
                    parentFragment.lifecycleScope.launch(Dispatchers.IO){
                        val checker = parentInterface.getFavByID(product.id).first() == null
                        withContext(Dispatchers.Main) {
                            navController.navigate(
                                MainFragmentDirections.actionMainFragmentToDescFragment(
                                    product.id,
                                    !checker
                                )
                            )
                        }
                    }
                }

                fabInCart.setOnClickListener {
                    parentInterface.addToCart(product)
                    Toast.makeText(context, "${product.name} has been added to cart!", Toast.LENGTH_SHORT).show()
                }

                fabFavorite.setOnClickListener {
                    parentFragment.lifecycleScope.launch(Dispatchers.IO) {
                        Log.d(LOG_DEBUG, parentInterface.getFavByID(product.id).first()?.toString() ?: "Not found with ${product.id}")
                        with(parentInterface.getFavByID(product.id).first() == null) {
                            if (this) parentInterface.insertFav(product)
                            else parentInterface.deleteFav(product)
                            withContext(Dispatchers.Main){
                                setButtonStyle(this@with)
                            }
                        }
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
                            R.drawable.ns_favorite_full
                        )
                    )
                    fabFavorite.backgroundTintList =
                        ColorStateList.valueOf(context.getColor(R.color.mild_orange))
                } else {
                    fabFavorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ns_like
                        )
                    )
                    fabFavorite.backgroundTintList =
                        ColorStateList.valueOf(context.getColor(R.color.white))
                }
            }
        }
    }


}

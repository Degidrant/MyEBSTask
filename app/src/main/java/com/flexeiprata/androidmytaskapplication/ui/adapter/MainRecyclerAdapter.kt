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
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.data.models.ProductPayloads
import com.flexeiprata.androidmytaskapplication.data.models.ProductUIModel
import com.flexeiprata.androidmytaskapplication.databinding.MainAdapterBinding
import com.flexeiprata.androidmytaskapplication.databinding.MainAdapterGridBinding
import com.flexeiprata.androidmytaskapplication.ui.common.Payloadable
import com.flexeiprata.androidmytaskapplication.utils.LOG_DEBUG

class MainRecyclerAdapter(
    val parentFragment: Fragment,
    private val spanCountChecker: (Unit) -> Int // Prosto po prikolu, chtob posmotreti kak rabotaiet
) : PagingDataAdapter<ProductUIModel, MainRecyclerAdapter.ViewBindingViewHolder>(MainDiffUtil) {

    private lateinit var context: Context
    private lateinit var parentInterface: FavoriteSwitch

    interface FavoriteSwitch {
        fun deleteFav(fav: Product)
        fun insertFav(fav: Product)
        fun addToCart(product: Product)
        fun navigateToNext(id: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewBindingViewHolder {
        context = parent.context
        parentInterface = parentFragment as FavoriteSwitch
        return if (viewType == 1) {
            val binding =
                MainAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            InnerViewHolder(binding)
        } else {
            val binding =
                MainAdapterGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            InnerGridViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: ViewBindingViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onBindViewHolder(
        holder: ViewBindingViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (!payloads.isNullOrEmpty()) {
            payloads.forEach { payloadList ->
                if (payloadList is List<*>) {
                    (payloadList as List<Payloadable>).forEach { payload ->
                        Log.d(LOG_DEBUG, "Here is a payload")
                        when (payload) {
                            is ProductPayloads.PriceChanged -> holder.updatePrice(payload.price)
                            is ProductPayloads.DescChanged -> holder.updateDesc(
                                String.format(
                                    "%1s\n%2s",
                                    payload.size,
                                    payload.color
                                )
                            )
                            is ProductPayloads.PicChanged -> holder.updateImage(payload.url)
                            is ProductPayloads.NameChanged -> holder.updateName(payload.name)
                            is ProductPayloads.FavChanged -> holder.updateButton(payload.isFav)
                        }
                    }
                }
            }
        } else {
            getItem(position)?.let {
                holder.bind(it)
            }
        }
    }

    abstract class ViewBindingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: ProductUIModel)
        abstract fun updatePrice(price: Int)
        abstract fun updateName(name: String)
        abstract fun updateImage(url: String)
        abstract fun updateDesc(desc: String)
        abstract fun updateButton(fav: Boolean)
    }

    object MainDiffUtil : DiffUtil.ItemCallback<ProductUIModel>() {

        override fun areItemsTheSame(oldItem: ProductUIModel, newItem: ProductUIModel): Boolean {
            return oldItem.isItemTheSame(newItem)
        }

        override fun areContentsTheSame(oldItem: ProductUIModel, newItem: ProductUIModel): Boolean {
            return oldItem.isContentTheSame(newItem)
        }

        override fun getChangePayload(
            oldItem: ProductUIModel,
            newItem: ProductUIModel
        ): List<Payloadable>? {
            val payloads = oldItem.payloads(newItem)
            return if (!payloads.isNullOrEmpty())
                payloads
            else
                null
        }
    }


    override fun getItemViewType(position: Int): Int {
        return spanCountChecker.invoke(Unit)
    }


    inner class InnerViewHolder(private val view: MainAdapterBinding) :
        ViewBindingViewHolder(view.root) {
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
                        context,
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
            view.apply {
                if (fav) {
                    fabFavorite.apply {
                        setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ns_favorite_full
                            )
                        )
                        imageTintList =
                            ColorStateList.valueOf(context.getColor(R.color.funny_orange))
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
                        imageTintList =
                            ColorStateList.valueOf(context.getColor(R.color.funny_orange))
                        backgroundTintList =
                            ColorStateList.valueOf(context.getColor(R.color.white))
                    }
                }
            }
        }
    }

    inner class InnerGridViewHolder(private val view: MainAdapterGridBinding) :
        ViewBindingViewHolder(view.root) {
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
                        context,
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
            view.apply {
                if (fav) {
                    fabFavorite.apply {
                        setImageDrawable(
                            ContextCompat.getDrawable(
                                context,
                                R.drawable.ns_favorite_full
                            )
                        )
                        imageTintList =
                            ColorStateList.valueOf(context.getColor(R.color.funny_orange))
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
                        imageTintList =
                            ColorStateList.valueOf(context.getColor(R.color.funny_orange))
                        backgroundTintList =
                            ColorStateList.valueOf(context.getColor(R.color.white))
                    }
                }
            }
        }
    }
}



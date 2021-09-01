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
import com.flexeiprata.androidmytaskapplication.databinding.MainAdapterBinding
import com.flexeiprata.androidmytaskapplication.databinding.MainAdapterGridBinding
import com.flexeiprata.androidmytaskapplication.ui.common.Payloadable
import com.flexeiprata.androidmytaskapplication.ui.models.ProductUIModel
import com.flexeiprata.androidmytaskapplication.ui.viewholders.GridProductsViewHolder
import com.flexeiprata.androidmytaskapplication.ui.viewholders.RowsProductsViewHolder
import com.flexeiprata.androidmytaskapplication.ui.viewholders.ViewBindingProductsViewHolder
import com.flexeiprata.androidmytaskapplication.utils.LOG_DEBUG

//  TODO: more about interfaces : https://www.programiz.com/kotlin-programming/interfaces

// TODO: I think will be better to rename this. For example: ProductsAdapter
class ProductsAdapter(
    fragment: Fragment,
    private val spanCountChecker: (Unit) -> Int // Prosto po prikolu, chtob posmotreti kak rabotaiet
) : PagingDataAdapter<ProductUIModel, ViewBindingProductsViewHolder>(MainDiffUtil) {

    //private lateinit var context: Context
    private var parentInterface: FavoriteSwitch

    init {
        parentInterface = fragment as FavoriteSwitch
    }

    // TODO: Better move on bottom of the class - it will not affect general view aspect

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewBindingProductsViewHolder {
        //context = parent.context
        val inflater = LayoutInflater.from(parent.context)
        // TODO: You can extract LayoutInflater in local variable to use for bose ViewHolders
        //  try to use 'when' instead of 'if' 
        return if (viewType == 1) {
            val binding =
                MainAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            RowsProductsViewHolder(binding, parentInterface)
        } else {
            val binding =
                MainAdapterGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            GridProductsViewHolder(binding, parentInterface)
        }
    }

    override fun onBindViewHolder(holder: ViewBindingProductsViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onBindViewHolder(
        holder: ViewBindingProductsViewHolder,
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

    interface FavoriteSwitch {
        fun deleteFav(fav: Product)
        fun insertFav(fav: Product)
        fun addToCart(product: Product)
        fun navigateToNext(id: Int)
    }

}



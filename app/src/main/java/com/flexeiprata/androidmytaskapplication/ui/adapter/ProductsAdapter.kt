package com.flexeiprata.androidmytaskapplication.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.ui.models.payloads.ProductPayloads
import com.flexeiprata.androidmytaskapplication.databinding.MainAdapterBinding
import com.flexeiprata.androidmytaskapplication.databinding.MainAdapterGridBinding
import com.flexeiprata.androidmytaskapplication.ui.common.Payloadable
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.ProductUIModel
import com.flexeiprata.androidmytaskapplication.ui.viewholders.ProductGridViewHolder
import com.flexeiprata.androidmytaskapplication.ui.viewholders.ProductRowsViewHolder
import com.flexeiprata.androidmytaskapplication.ui.viewholders.ProductAbstractVH
import com.flexeiprata.androidmytaskapplication.utils.LOG_DEBUG

class ProductsAdapter(
    fragment: Fragment
) : PagingDataAdapter<ProductUIModel, ProductAbstractVH>(MainDiffUtil) {

    private val parentInterface: FavoriteSwitch

    init {
        parentInterface = fragment as FavoriteSwitch
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductAbstractVH {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType){
            1 -> {
                val binding =
                    MainAdapterBinding.inflate(inflater, parent, false)
                ProductRowsViewHolder(binding, parentInterface)
            }
            else -> {
                val binding =
                    MainAdapterGridBinding.inflate(inflater, parent, false)
                ProductGridViewHolder(binding, parentInterface)
            }
        }
    }

    override fun onBindViewHolder(holder: ProductAbstractVH, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onBindViewHolder(
        holder: ProductAbstractVH,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (!payloads.isNullOrEmpty()) {
            payloads.forEach { payloadList ->
                if (payloadList is List<*>) {
                    payloadList.forEach { payload ->
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
        return parentInterface.getSpanCount()
    }

    interface FavoriteSwitch {
        fun deleteFav(fav: Product)
        fun insertFav(fav: Product)
        fun addToCart(product: Product)
        fun navigateToNext(id: Int)
        fun getSpanCount() : Int
    }

}



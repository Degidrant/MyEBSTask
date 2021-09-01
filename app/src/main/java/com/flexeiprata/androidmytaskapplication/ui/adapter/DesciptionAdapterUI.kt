package com.flexeiprata.androidmytaskapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentBody2Binding
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentBodyBinding
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentHeaderBinding
import com.flexeiprata.androidmytaskapplication.ui.models.payloads.DescUIPayloads
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.DescUIModel
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.RowDescUI
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.RowHeaderUI
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.RowMainUI
import com.flexeiprata.androidmytaskapplication.ui.viewholders.DescAbstractVH
import com.flexeiprata.androidmytaskapplication.ui.viewholders.DescriptionViewHolder
import com.flexeiprata.androidmytaskapplication.ui.viewholders.HeaderViewHolder
import com.flexeiprata.androidmytaskapplication.ui.viewholders.MainContentViewHolder

class DesciptionAdapterUI(
    private val listOfModels: MutableList<DescUIModel>
) :
    ListAdapter<DescUIModel, DescAbstractVH>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<DescUIModel>() {
        override fun areItemsTheSame(oldItem: DescUIModel, newItem: DescUIModel): Boolean {
            return oldItem.id() == newItem.id()
        }

        override fun areContentsTheSame(oldItem: DescUIModel, newItem: DescUIModel): Boolean {
            return oldItem.equality(newItem)
        }

        override fun getChangePayload(oldItem: DescUIModel, newItem: DescUIModel): Any? {
            val payloads = oldItem.payloads(newItem)
            return if (!payloads.isNullOrEmpty())
                payloads
            else
                null
        }

    }

    companion object {
        const val HEADER = 0
        const val BODY_MAIN = 1
        const val BODY_DESC = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (listOfModels[position]) {
            is RowHeaderUI -> HEADER
            is RowMainUI -> BODY_MAIN
            is RowDescUI -> BODY_DESC
            else -> throw Exception("Invalid viewHolder exception")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DescAbstractVH {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HEADER -> HeaderViewHolder(DescFragmentHeaderBinding.inflate(inflater, parent, false))
            BODY_MAIN -> MainContentViewHolder(
                DescFragmentBodyBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            BODY_DESC -> DescriptionViewHolder(
                DescFragmentBody2Binding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            else -> throw Exception("onCreateViewHolder inflate exception")
        }
    }

    override fun onBindViewHolder(holder: DescAbstractVH, position: Int, payloads: MutableList<Any>) {
        if (!payloads.isNullOrEmpty()) {
            payloads.forEach { payloadList ->
                if (payloadList is List<*>) {
                    payloadList.forEach { payload ->
                        when (payload) {
                            is DescUIPayloads.PriceChanged -> holder.updatePrice(payload.price)
                            is DescUIPayloads.DescChanged -> holder.updateDesc(payload.shortDesc)
                            is DescUIPayloads.ImageChanged -> holder.updateImage(payload.image)
                            is DescUIPayloads.TitleChanged -> holder.updateTitle(payload.title)
                            is DescUIPayloads.LongDescChanged -> holder.updateTitle(payload.desc)
                        }
                    }
                }
            }
        } else {
                holder.bind(listOfModels[position])
            }
        }

    override fun getItemCount(): Int = listOfModels.size

    override fun onBindViewHolder(holder: DescAbstractVH, position: Int) {
        holder.bind(listOfModels[position])
    }

}
package com.flexeiprata.androidmytaskapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentBody2Binding
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentBodyBinding
import com.flexeiprata.androidmytaskapplication.databinding.DescFragmentHeaderBinding
import com.flexeiprata.androidmytaskapplication.ui.models.DescUIModel
import com.flexeiprata.androidmytaskapplication.ui.models.RowDescUI
import com.flexeiprata.androidmytaskapplication.ui.models.RowHeaderUI
import com.flexeiprata.androidmytaskapplication.ui.models.RowMainUI

class DescUIRecyclerAdapter(
    private val listOfModels: MutableList<DescUIModel>
) :
    ListAdapter<DescUIModel, BasicViewBindingViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<DescUIModel>() {
        override fun areItemsTheSame(oldItem: DescUIModel, newItem: DescUIModel): Boolean {
            return oldItem.id() == newItem.id()
        }

        override fun areContentsTheSame(oldItem: DescUIModel, newItem: DescUIModel): Boolean {
            return oldItem.equality(newItem)
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
    ): BasicViewBindingViewHolder {
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

    override fun onBindViewHolder(holder: BasicViewBindingViewHolder, position: Int) {
        holder.bind(listOfModels[position])
    }

    override fun getItemCount(): Int = listOfModels.size

}
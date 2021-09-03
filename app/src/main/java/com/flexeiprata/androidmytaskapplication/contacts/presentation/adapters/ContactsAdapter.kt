package com.flexeiprata.androidmytaskapplication.contacts.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flexeiprata.androidmytaskapplication.contacts.presentation.uimodels.ContactsPayloads
import com.flexeiprata.androidmytaskapplication.contacts.presentation.uimodels.ContactsUIModel
import com.flexeiprata.androidmytaskapplication.databinding.ContactAdapterBinding
import com.flexeiprata.androidmytaskapplication.ui.common.Payloadable

class ContactsAdapter :
    ListAdapter<ContactsUIModel, ContactsAdapter.ContactViewHolder>(ContactsDiffUtil) {

    private var shareWith: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ContactViewHolder(ContactAdapterBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun onBindViewHolder(
        holder: ContactViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (!payloads.isNullOrEmpty()) {
            payloads.forEach { payloadList ->
                if (payloadList is List<*>) {
                    payloadList.forEach { payload ->
                        when (payload) {
                            is ContactsPayloads.NameChanged -> holder.updateName(payload.name)
                            is ContactsPayloads.NumberChanged -> holder.updateNumber(payload.number)
                        }
                    }
                }
            }
        } else {
            holder.bind(currentList[position])
        }
    }

    fun setShareListener(action: (number: String) -> Unit){
        shareWith = action
    }

    object ContactsDiffUtil : DiffUtil.ItemCallback<ContactsUIModel>() {
        override fun areItemsTheSame(oldItem: ContactsUIModel, newItem: ContactsUIModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ContactsUIModel,
            newItem: ContactsUIModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: ContactsUIModel, newItem: ContactsUIModel):
                List<Payloadable>? {
            val payloads = oldItem.payloads(newItem)
            return if (!payloads.isNullOrEmpty())
                payloads
            else
                null
        }
    }

    inner class ContactViewHolder(private val view: ContactAdapterBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(contact: ContactsUIModel) {
            updateName(contact.displayedName)
            updateNumber(contact.phoneNumber)
            itemView.setOnClickListener {
                shareWith?.invoke(contact.phoneNumber)
            }
        }

        fun updateName(displayedName: String) {
            view.contactName.text = displayedName
        }

        fun updateNumber(displayedNumber: String) {
            view.contactName2.text = displayedNumber
        }
    }


}

package com.flexeiprata.androidmytaskapplication.contacts.presentation.uimodels

import com.flexeiprata.androidmytaskapplication.common.Payloadable
import com.flexeiprata.androidmytaskapplication.description.presentation.views.uimodels.RowItem

data class ContactsUIModel(val id: String, val displayedName: String, val phoneNumber: String) :
    RowItem() {

    override fun id(): Any {
        return id
    }

    override fun equality(other: RowItem): Boolean {
        return false
    }

    override fun payloads(other: RowItem): List<Payloadable> {
        val payloads = mutableListOf<Payloadable>()
        if (other is ContactsUIModel) {
            if (displayedName != other.displayedName) payloads.add(
                ContactsPayloads.NameChanged(
                    other.displayedName
                )
            )
            if (phoneNumber != other.phoneNumber) payloads.add(ContactsPayloads.NumberChanged(other.phoneNumber))
        }
        return payloads
    }
}
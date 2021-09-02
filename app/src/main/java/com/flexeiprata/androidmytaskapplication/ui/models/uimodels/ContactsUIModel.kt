package com.flexeiprata.androidmytaskapplication.ui.models.uimodels

import com.flexeiprata.androidmytaskapplication.ui.common.Payloadable
import com.flexeiprata.androidmytaskapplication.ui.models.payloads.ContactsPayloads

data class ContactsUIModel(val id: String, val displayedName: String, val phoneNumber: String){
    fun payloads(other: ContactsUIModel) : List<Payloadable>{
        val payloads = mutableListOf<Payloadable>()
        if (displayedName != other.displayedName) payloads.add(ContactsPayloads.NameChanged(other.displayedName))
        if (phoneNumber != other.phoneNumber) payloads.add(ContactsPayloads.NumberChanged(other.phoneNumber))
        return payloads
    }
}
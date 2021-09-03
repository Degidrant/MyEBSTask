package com.flexeiprata.androidmytaskapplication.contacts.presentation.uimodels

import com.flexeiprata.androidmytaskapplication.common.Payloadable

sealed class ContactsPayloads {
    data class NameChanged(val name: String) : Payloadable
    data class NumberChanged(val number: String) : Payloadable
}
package com.flexeiprata.androidmytaskapplication.ui.models.payloads

import com.flexeiprata.androidmytaskapplication.ui.common.Payloadable

sealed class ContactsPayloads {
    data class NameChanged(val name: String) : Payloadable
    data class NumberChanged(val number: String) : Payloadable
}
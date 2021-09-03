package com.flexeiprata.androidmytaskapplication.contacts.presentation.views

import com.flexeiprata.androidmytaskapplication.contacts.presentation.uimodels.ContactsUIModel

sealed class ContactsResult{
    data class Loading (val data: List<ContactsUIModel?>, val message: String = "") : ContactsResult()
    data class Success (val data: List<ContactsUIModel?>, val message: String = "") : ContactsResult()
    data class Error (val message: String, val exception: Exception? = null) : ContactsResult()
}

package com.flexeiprata.androidmytaskapplication.contacts.domain

import com.flexeiprata.androidmytaskapplication.contacts.presentation.uimodels.ContactsUIModel

interface ContactsRepository {
    fun getContacts() : List<ContactsUIModel>
}
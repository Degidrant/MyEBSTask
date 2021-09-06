package com.flexeiprata.androidmytaskapplication.contacts.domain

import com.flexeiprata.androidmytaskapplication.contacts.presentation.uimodels.ContactsUIModel
import io.reactivex.rxjava3.core.Single

interface ContactsRepository {
    fun getContacts() : Single<List<ContactsUIModel>>
}
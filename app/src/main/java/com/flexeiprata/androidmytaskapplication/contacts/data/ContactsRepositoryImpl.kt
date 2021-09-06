package com.flexeiprata.androidmytaskapplication.contacts.data

import android.app.Application
import com.flexeiprata.androidmytaskapplication.contacts.domain.ContactsRepository
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(context: Application) : ContactsRepository {

    private val contactsContentProvider = ContactsContentProvider(context)
    override fun getContacts() = contactsContentProvider.getContactsRx()
}
package com.flexeiprata.androidmytaskapplication.ui.main

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.ContactsUIModel
import com.flexeiprata.androidmytaskapplication.ui.observers.ContentContactsObserver
import com.flexeiprata.androidmytaskapplication.utils.ContactsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val app: Application) : AndroidViewModel(app) {
    private val contactHelper = ContactsHelper(app)

    private lateinit var contactListValue: MutableList<ContactsUIModel>
    private val mutableContactList = MutableStateFlow(emptyList<ContactsUIModel>())
    val contactList = mutableContactList.asStateFlow()
    private val contactsObserver = ContentContactsObserver(contactHelper, mutableContactList)

    fun registerObserver(){
        app.contentResolver.registerContentObserver(
            ContactsContract.Contacts.CONTENT_URI,
            true,
            contactsObserver
        )
    }

    fun registerStateFlow(){
        viewModelScope.launch(Dispatchers.IO) {
            contactListValue = contactHelper.getContacts()
            mutableContactList.value = contactListValue
        }
    }

    fun unregisterObserver(){
        app.contentResolver.unregisterContentObserver(contactsObserver)
    }

}
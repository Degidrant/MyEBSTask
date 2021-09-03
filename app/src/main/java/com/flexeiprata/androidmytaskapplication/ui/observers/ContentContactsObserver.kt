package com.flexeiprata.androidmytaskapplication.ui.observers

import android.database.ContentObserver
import android.net.Uri
import android.util.Log
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.ContactsUIModel
import com.flexeiprata.androidmytaskapplication.utils.ContactsHelper
import com.flexeiprata.androidmytaskapplication.utils.LOG_DEBUG
import kotlinx.coroutines.flow.MutableStateFlow

class ContentContactsObserver(private val contactsHelper: ContactsHelper, private val source: MutableStateFlow<List<ContactsUIModel>>) : ContentObserver(null) {

    init {
        Log.d(LOG_DEBUG, "Object's created")
    }
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        Log.d(LOG_DEBUG, "Changes are found")
        source.value = contactsHelper.getContacts()
    }

    override fun onChange(selfChange: Boolean) {
        this.onChange(selfChange, null)
    }

}
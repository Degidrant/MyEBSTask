package com.flexeiprata.androidmytaskapplication.contacts.utils

import android.database.ContentObserver
import android.net.Uri
import android.util.Log
import com.flexeiprata.androidmytaskapplication.common.LOG_DEBUG
import com.flexeiprata.androidmytaskapplication.contacts.presentation.uimodels.ContactsUIModel
import com.flexeiprata.androidmytaskapplication.contacts.presentation.usecases.GetAllContactsUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class ContentContactsObserver(private val getAllContactsUseCase: GetAllContactsUseCase, private val source: MutableStateFlow<List<ContactsUIModel>>) : ContentObserver(null) {

    init {
        Log.d(LOG_DEBUG, "Object's created")
    }
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        Log.d(LOG_DEBUG, "Changes are found")
        source.value = getAllContactsUseCase()
    }

    override fun onChange(selfChange: Boolean) {
        this.onChange(selfChange, null)
    }

}
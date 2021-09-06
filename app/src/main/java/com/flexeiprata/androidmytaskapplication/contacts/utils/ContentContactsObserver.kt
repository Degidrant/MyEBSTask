package com.flexeiprata.androidmytaskapplication.contacts.utils

import android.database.ContentObserver
import android.net.Uri
import android.util.Log
import com.flexeiprata.androidmytaskapplication.common.LOG_DEBUG
import com.flexeiprata.androidmytaskapplication.contacts.presentation.usecases.GetAllContactsUseCase
import com.flexeiprata.androidmytaskapplication.contacts.presentation.views.ContactsResult
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow

class ContentContactsObserver(private val getAllContactsUseCase: GetAllContactsUseCase, private val source: MutableStateFlow<ContactsResult>) : ContentObserver(null) {

    init {
        Log.d(LOG_DEBUG, "Object's created")
    }
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        Log.d(LOG_DEBUG, "Changes are found")
        getAllContactsUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {source.value = ContactsResult.Success(it) })
    }

    override fun onChange(selfChange: Boolean) {
        this.onChange(selfChange, null)
    }

}
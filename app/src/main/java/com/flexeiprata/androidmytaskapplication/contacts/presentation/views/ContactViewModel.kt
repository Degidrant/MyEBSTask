package com.flexeiprata.androidmytaskapplication.contacts.presentation.views

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import com.flexeiprata.androidmytaskapplication.contacts.presentation.usecases.GetAllContactsUseCase
import com.flexeiprata.androidmytaskapplication.contacts.utils.ContentContactsObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val getAllContactsUseCase: GetAllContactsUseCase) :
    ViewModel() {

    private val mutableContactList = MutableStateFlow<ContactsResult>(
        ContactsResult.Loading(
            emptyList()
        )
    )
    val contactList = mutableContactList.asStateFlow()

    private val contactsObserver =
        ContentContactsObserver(getAllContactsUseCase, mutableContactList)

    fun registerObserver(context: Context) {
        context.contentResolver.registerContentObserver(
            ContactsContract.Contacts.CONTENT_URI,
            true,
            contactsObserver
        )
    }

    fun registerRX() {
        getAllContactsUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer { mutableContactList.value = ContactsResult.Success(it) })

    }

    fun unregisterObserver(context: Context) {
        context.contentResolver.unregisterContentObserver(contactsObserver)
    }

}
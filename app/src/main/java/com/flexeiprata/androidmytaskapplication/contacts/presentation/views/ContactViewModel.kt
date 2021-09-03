package com.flexeiprata.androidmytaskapplication.contacts.presentation.views

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flexeiprata.androidmytaskapplication.contacts.presentation.uimodels.ContactsUIModel
import com.flexeiprata.androidmytaskapplication.contacts.presentation.usecases.GetAllContactsUseCase
import com.flexeiprata.androidmytaskapplication.contacts.utils.ContentContactsObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val getAllContactsUseCase: GetAllContactsUseCase) : ViewModel() {

    //StateFlow
    private val mutableContactList = MutableStateFlow<ContactsResult>(
        ContactsResult.Loading(
        emptyList()))
    val contactList = mutableContactList.asStateFlow()

    private lateinit var contactListValue: List<ContactsUIModel>
    private val contactsObserver = ContentContactsObserver(getAllContactsUseCase, mutableContactList)

    fun registerObserver(context: Context){
        context.contentResolver.registerContentObserver(
            ContactsContract.Contacts.CONTENT_URI,
            true,
            contactsObserver
        )
    }

    fun registerStateFlow(){
        viewModelScope.launch(Dispatchers.IO) {
            contactListValue = getAllContactsUseCase()
            mutableContactList.value = ContactsResult.Success(contactListValue)
        }
    }

    fun unregisterObserver(context: Context){
        context.contentResolver.unregisterContentObserver(contactsObserver)
    }

}
package com.flexeiprata.androidmytaskapplication.ui.main

import android.annotation.SuppressLint
import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.ContactsUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val app: Application) : AndroidViewModel(app) {
    // TODO: Not a good solution. Every time you will get "contactList" -> a new content provider look up will happen. 
    // TODO: Wrap contact list into LiveData or SharedFlow/StateFlow
    val contactList = getContacts()


    // TODO: Make db query async (background thread)

    @SuppressLint("Range")
    private fun getContacts(): MutableList<ContactsUIModel> {
        val listOfContacts = mutableListOf<ContactsUIModel>()
        val sortOrder = "${ContactsContract.Contacts.Entity.DISPLAY_NAME} ASC"
        val projection =
            arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME)
        val selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " > ?"
        val selectionArgs = arrayOf("0")
        val cursorQueue = app.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
        cursorQueue?.let { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val cursorPhone = app.contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    arrayOf(id),
                    null
                )
                cursorPhone?.let {
                    while (cursorPhone.moveToNext()) {
                        val phoneNumValue = cursorPhone.getString(
                            cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )
                        listOfContacts.add(ContactsUIModel(id, name, phoneNumValue))
                    }
                    cursorPhone.close()
                }
            }
        }
        cursorQueue?.close()
        return listOfContacts
    }
}
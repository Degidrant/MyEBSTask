package com.flexeiprata.androidmytaskapplication.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.ViewModel
import com.flexeiprata.androidmytaskapplication.ui.models.uimodels.ContactsUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val context: Context) : ViewModel() {

    val contactList = getContacts()

    @SuppressLint("Range")
    private fun getContacts(): MutableList<ContactsUIModel> {
        val listOfContacts = mutableListOf<ContactsUIModel>()
        val sortOrder = "${ContactsContract.Contacts.Entity.DISPLAY_NAME} ASC"
        val cursorQueue = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            sortOrder
        )
        cursorQueue?.let { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber = (cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                )).toInt()
                if (hasPhoneNumber > 0) {
                    val cursorPhone = context.contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
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
        }
        return listOfContacts
    }
}
package com.flexeiprata.androidmytaskapplication.contacts.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.provider.ContactsContract
import android.util.Log
import com.flexeiprata.androidmytaskapplication.contacts.presentation.uimodels.ContactsUIModel
import com.flexeiprata.androidmytaskapplication.utils.LOG_DEBUG
import javax.inject.Inject

class ContactsContentProvider @Inject constructor(private val context: Context) {

    @SuppressLint("Range")
    fun getContacts(): MutableList<ContactsUIModel> {
        Log.d(LOG_DEBUG, "It is main thread? Answer is " + (Looper.myLooper() == Looper.getMainLooper()).toString())
        val listOfContacts = mutableListOf<ContactsUIModel>()
        val sortOrder = "${ContactsContract.Contacts.Entity.DISPLAY_NAME} ASC"
        val projection =
            arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME)
        val selection = ContactsContract.Contacts.HAS_PHONE_NUMBER + " > ?"
        val selectionArgs = arrayOf("0")
        val cursorQueue = context.contentResolver.query(
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
                val cursorPhone = context.contentResolver.query(
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
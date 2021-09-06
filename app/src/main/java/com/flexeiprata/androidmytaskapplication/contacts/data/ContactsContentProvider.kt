package com.flexeiprata.androidmytaskapplication.contacts.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.provider.ContactsContract
import android.util.Log
import com.flexeiprata.androidmytaskapplication.common.LOG_DEBUG
import com.flexeiprata.androidmytaskapplication.contacts.presentation.uimodels.ContactsUIModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ContactsContentProvider @Inject constructor(private val context: Context) {

    @SuppressLint("Range")
    private fun getContacts(): MutableList<ContactsUIModel> {
        Log.d(
            LOG_DEBUG,
            "It is main thread? Answer is " + (Looper.myLooper() == Looper.getMainLooper()).toString()
        )
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

    fun getContactsRx(): Single<List<ContactsUIModel>> {
        return Single.create { emitter ->
            try {
                val contacts = getContacts()
                emitter.onSuccess(contacts)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    /*fun getOneContact() : Observable<ContactsUIModel> {
        return getContactsRx().flatMapObservable {contacts ->
            Observable.fromIterable(contacts)
        }
    }

    fun listenOneContact() {
        getOneContact().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ContactsUIModel>{
                override fun onSubscribe(d: Disposable?) {

                }

                override fun onNext(t: ContactsUIModel?) {

                }

                override fun onError(e: Throwable?) {
                }

                override fun onComplete() {
                }

            })
    }*/

}
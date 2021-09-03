package com.flexeiprata.androidmytaskapplication.contacts.presentation.usecases

import com.flexeiprata.androidmytaskapplication.contacts.presentation.uimodels.ContactsUIModel

interface GetAllContactsUseCase {
    operator fun invoke() : List<ContactsUIModel>
}
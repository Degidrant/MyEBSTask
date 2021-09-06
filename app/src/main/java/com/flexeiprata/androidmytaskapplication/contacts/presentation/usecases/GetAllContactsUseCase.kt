package com.flexeiprata.androidmytaskapplication.contacts.presentation.usecases

import com.flexeiprata.androidmytaskapplication.contacts.presentation.uimodels.ContactsUIModel
import io.reactivex.rxjava3.core.Single

interface GetAllContactsUseCase {
    operator fun invoke() : Single<List<ContactsUIModel>>
}
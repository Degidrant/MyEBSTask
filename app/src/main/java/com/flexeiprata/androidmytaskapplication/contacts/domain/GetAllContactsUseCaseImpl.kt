package com.flexeiprata.androidmytaskapplication.contacts.domain

import com.flexeiprata.androidmytaskapplication.contacts.presentation.uimodels.ContactsUIModel
import com.flexeiprata.androidmytaskapplication.contacts.presentation.usecases.GetAllContactsUseCase
import javax.inject.Inject

class GetAllContactsUseCaseImpl @Inject constructor(private val repository: ContactsRepository) :
    GetAllContactsUseCase {
    override fun invoke(): List<ContactsUIModel> = repository.getContacts()

}
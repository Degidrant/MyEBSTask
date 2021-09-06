package com.flexeiprata.androidmytaskapplication.contacts.domain

import com.flexeiprata.androidmytaskapplication.contacts.presentation.uimodels.ContactsUIModel
import com.flexeiprata.androidmytaskapplication.contacts.presentation.usecases.GetAllContactsUseCase
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetAllContactsUseCaseImpl @Inject constructor(private val repository: ContactsRepository) :
    GetAllContactsUseCase {
    override fun invoke(): Single<List<ContactsUIModel>> = repository.getContacts()

}
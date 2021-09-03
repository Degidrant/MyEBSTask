package com.flexeiprata.androidmytaskapplication.modules.usecasesmodules

import com.flexeiprata.androidmytaskapplication.contacts.domain.GetAllContactsUseCaseImpl
import com.flexeiprata.androidmytaskapplication.contacts.presentation.usecases.GetAllContactsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class ContactsUseCasesModule {
    @Binds
    abstract fun bindsContactsUseCase(implementation: GetAllContactsUseCaseImpl) : GetAllContactsUseCase
}
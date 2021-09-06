package com.flexeiprata.androidmytaskapplication.modules.usecasesmodules

import com.flexeiprata.androidmytaskapplication.favorites.domain.usecaseimpl.*
import com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class FavUseCaseModule {
    @Binds
    abstract fun bindsActualizeUseCase(implementation: ActualizeUseCaseImpl): ActualizeUseCase

    @Binds
    abstract fun bindsDeleteFavUseCase(implementation: DeleteFavUseCaseImpl): DeleteFavUseCase

    @Binds
    abstract fun bindsGetFavByIdUseCase(implementation: GetFavByIDUseCaseImpl): GetFavByIdUseCase

    @Binds
    abstract fun bindsInsertFavUseCase(implementation: InsertFavUseCaseImpl): InsertFavUseCase

    @Binds
    abstract fun bindsGetAllFavsRXUseCase(implementation: GetAllFavsRXUseCaseImpl): GetAllFavsRXUseCase

    @Binds
    abstract fun bindsInsertFavUseCaseCo(implementation: InsertFavUseCaseCoImpl): InsertFavUseCaseCo
}
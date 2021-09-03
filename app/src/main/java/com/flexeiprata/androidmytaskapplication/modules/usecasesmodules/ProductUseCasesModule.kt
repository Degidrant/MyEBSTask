package com.flexeiprata.androidmytaskapplication.modules.usecasesmodules

import com.flexeiprata.androidmytaskapplication.description.domain.GetProductUseCaseImpl
import com.flexeiprata.androidmytaskapplication.description.presentation.usecases.GetProductUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class ProductUseCasesModule {
    @Binds
    abstract fun bindsGetProductsUseCase(implementation: GetProductUseCaseImpl): GetProductUseCase
}
package com.flexeiprata.androidmytaskapplication.modules.usecasesmodules

import com.flexeiprata.androidmytaskapplication.products.domain.GetProductsUseCaseImpl
import com.flexeiprata.androidmytaskapplication.products.presentation.usecases.GetProductsUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class DescUseCasesModule {
    @Binds
    abstract fun bindsGetProductUseCase(implementation : GetProductsUseCaseImpl) : GetProductsUseCase
}
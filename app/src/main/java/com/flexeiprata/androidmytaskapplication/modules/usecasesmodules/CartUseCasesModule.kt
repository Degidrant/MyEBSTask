package com.flexeiprata.androidmytaskapplication.modules.usecasesmodules

import com.flexeiprata.androidmytaskapplication.cart.domain.AddToCartUseCaseImpl
import com.flexeiprata.androidmytaskapplication.cart.domain.ClearCartUseCaseImpl
import com.flexeiprata.androidmytaskapplication.cart.domain.GetCartUseCaseImpl
import com.flexeiprata.androidmytaskapplication.cart.presentation.AddToCartUseCase
import com.flexeiprata.androidmytaskapplication.cart.presentation.ClearCartUseCase
import com.flexeiprata.androidmytaskapplication.cart.presentation.GetCartUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class CartUseCasesModule {
    @Binds
    abstract fun bindAddToCartUseCase(implementation : AddToCartUseCaseImpl) : AddToCartUseCase

    @Binds
    abstract fun bindClearCartUseCase(implementation : ClearCartUseCaseImpl) : ClearCartUseCase

    @Binds
    abstract fun bindGetCartUseCase(implementation: GetCartUseCaseImpl) : GetCartUseCase

}
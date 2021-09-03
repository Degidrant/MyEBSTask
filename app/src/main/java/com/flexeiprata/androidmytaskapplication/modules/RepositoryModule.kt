package com.flexeiprata.androidmytaskapplication.modules

import com.flexeiprata.androidmytaskapplication.cart.data.CartRepositoryImpl
import com.flexeiprata.androidmytaskapplication.cart.domain.CartRepository
import com.flexeiprata.androidmytaskapplication.contacts.data.ContactsRepositoryImpl
import com.flexeiprata.androidmytaskapplication.contacts.domain.ContactsRepository
import com.flexeiprata.androidmytaskapplication.description.data.DescRepositoryImpl
import com.flexeiprata.androidmytaskapplication.description.domain.DescRepository
import com.flexeiprata.androidmytaskapplication.favorites.data.FavoritesRepositoryImpl
import com.flexeiprata.androidmytaskapplication.favorites.domain.FavoritesRepository
import com.flexeiprata.androidmytaskapplication.products.data.api.ProductsRepositoryImpl
import com.flexeiprata.androidmytaskapplication.products.domain.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindsContactsRepository(impl: ContactsRepositoryImpl): ContactsRepository

    @Binds
    abstract fun bindsFavRepository(impl: FavoritesRepositoryImpl): FavoritesRepository

    @Binds
    abstract fun bindsDescRepository(impl: DescRepositoryImpl): DescRepository

    @Binds
    abstract fun bindsProductRepository(impl: ProductsRepositoryImpl): ProductsRepository

    @Binds
    abstract fun bindsCartRepository(impl: CartRepositoryImpl): CartRepository

}
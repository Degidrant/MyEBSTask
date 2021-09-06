package com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases

import io.reactivex.rxjava3.core.Completable

interface DeleteFavUseCase {
    operator fun invoke(id: Int): Completable
}
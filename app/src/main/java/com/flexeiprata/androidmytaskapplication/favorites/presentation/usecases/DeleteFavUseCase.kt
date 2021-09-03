package com.flexeiprata.androidmytaskapplication.favorites.presentation.usecases

interface DeleteFavUseCase {
    suspend operator fun invoke(id: Int)
}
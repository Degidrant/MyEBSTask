package com.flexeiprata.androidmytaskapplication.description.domain

import com.flexeiprata.androidmytaskapplication.description.presentation.usecases.GetProductUseCase
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import javax.inject.Inject

class GetProductUseCaseImpl @Inject constructor(private val descRepository: DescRepository) :
    GetProductUseCase {
    override suspend fun invoke(id: Int): Product = descRepository.getProductById(id)

}
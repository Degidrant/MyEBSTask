package com.flexeiprata.androidmytaskapplication.description.domain

import com.flexeiprata.androidmytaskapplication.description.presentation.usecases.GetProductUseCase
import javax.inject.Inject

class GetProductUseCaseImpl @Inject constructor(private val descRepository: DescRepository) :
    GetProductUseCase {
    override fun invoke(id: Int) = descRepository.getProductById(id)

}
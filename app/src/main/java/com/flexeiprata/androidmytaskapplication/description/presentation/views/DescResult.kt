package com.flexeiprata.androidmytaskapplication.description.presentation.views

import com.flexeiprata.androidmytaskapplication.description.presentation.views.uimodels.RowItem

sealed class DescResult {
    data class Loading(val data: List<RowItem?>, val message: String = "") : DescResult()
    data class Success(val data: List<RowItem?>, val message: String = "") : DescResult()
    data class Error(val message: String, val exception: Exception? = null) : DescResult()
}
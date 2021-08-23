package com.flexeiprata.androidmytaskapplication.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.flexeiprata.androidmytaskapplication.data.repository.LocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(private val repository: LocalRepository) : ViewModel() {

    fun getAllFav() = viewModelScope.launch {
        repository.getAllFav().asLiveData()
    }

}
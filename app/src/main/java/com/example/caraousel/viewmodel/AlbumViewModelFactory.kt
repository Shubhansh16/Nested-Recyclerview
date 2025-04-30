package com.example.caraousel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.caraousel.repository.AlbumRepository

class AlbumViewModelFactory(private val repository: AlbumRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlbumViewModel(repository) as T
    }
}
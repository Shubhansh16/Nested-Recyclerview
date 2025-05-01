package com.example.caraousel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.caraousel.repository.ChartRepository

class ChartViewModelFactory(
    private val repository: ChartRepository
) :ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChartViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
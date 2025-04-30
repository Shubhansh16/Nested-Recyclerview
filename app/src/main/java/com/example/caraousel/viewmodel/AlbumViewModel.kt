package com.example.caraousel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caraousel.model.Album
import com.example.caraousel.repository.AlbumRepository
import kotlinx.coroutines.launch

class AlbumViewModel(private val repository: AlbumRepository):ViewModel() {

    private val _albums = MutableLiveData<List<Album>>()
    val albums:LiveData<List<Album>> get() = _albums

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    init {
        fetchAlbumData()
    }

    private fun fetchAlbumData() {
        viewModelScope.launch {
            try {
                _albums.value = repository.fetchAlbums()
            } catch (e:Exception){
                _error.value ="Failed to load albums: ${e.localizedMessage}"
            }
        }
    }

}
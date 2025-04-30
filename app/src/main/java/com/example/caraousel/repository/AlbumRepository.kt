package com.example.caraousel.repository

import com.example.caraousel.RetrofitClient
import com.example.caraousel.model.Album

class AlbumRepository {
    suspend fun fetchAlbums():List<Album>{
        return RetrofitClient.apiService.getAlbums().data.map { it.album }
    }
}
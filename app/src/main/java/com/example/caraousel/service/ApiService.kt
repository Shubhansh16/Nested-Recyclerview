package com.example.caraousel.service


import com.example.caraousel.model.TrackResponse
import retrofit2.http.GET

interface ApiService {
    @GET("chart/0/tracks")
    suspend fun getAlbums():TrackResponse
}
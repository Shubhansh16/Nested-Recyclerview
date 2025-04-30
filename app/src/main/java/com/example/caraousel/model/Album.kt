package com.example.caraousel.model

data class TrackResponse(
    val data: List<Track>
)

data class Track(
    val album: Album
)

data class Album(
    val id: Long,
    val title: String,
    val cover_medium: String
)
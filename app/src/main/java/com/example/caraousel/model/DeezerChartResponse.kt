package com.example.caraousel.model

import com.google.gson.annotations.SerializedName

data class DeezerChartResponse(
    @SerializedName("tracks") val tracks: TrackData?,
    @SerializedName("albums") val albums: AlbumData?,
    @SerializedName("artists") val artists: ArtistData?
)

//Track Data
data class TrackData(
    @SerializedName("data") val data: List<ChartTrack>? // Renamed to avoid clash
)

data class ChartTrack(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String?,
    @SerializedName("artist") val artist: ArtistInfo?,
    @SerializedName("album") val album: AlbumInfo?
)

//Album Data
data class AlbumData(
    @SerializedName("data") val data: List<ChartAlbum>?
)

data class ChartAlbum(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String?,
    @SerializedName("cover_medium") val coverMedium: String?,
    @SerializedName("artist") val artist: ArtistInfo?
)

//Artist Data
data class ArtistData(
    @SerializedName("data") val data: List<ChartArtist>? // Renamed
)

data class ChartArtist(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String?,
    @SerializedName("picture_medium") val pictureMedium: String?
)

//Common Nested Info
data class ArtistInfo(
    @SerializedName("name") val name: String?
)

data class AlbumInfo(
    @SerializedName("id") val id: Long?,
    @SerializedName("title") val title: String?,
    @SerializedName("cover_medium") val coverMedium: String?
)

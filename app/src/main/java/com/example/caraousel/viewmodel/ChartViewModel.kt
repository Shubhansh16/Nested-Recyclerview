package com.example.caraousel.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caraousel.model.ChartItemType
import com.example.caraousel.model.DeezerChartResponse
import com.example.caraousel.model.DisplayableChartItem
import com.example.caraousel.model.OuterListItem
import com.example.caraousel.repository.ChartRepository
import kotlinx.coroutines.launch
import kotlin.math.log

data class ChartCategory(
    val title:String,
    val items: List<DisplayableChartItem>
)

class ChartViewModel(private val repository: ChartRepository):ViewModel() {

    private val _outerListItems = MutableLiveData<List<OuterListItem>>()
    val outerListItems: LiveData<List<OuterListItem>> = _outerListItems

    private val _categories = MutableLiveData<List<ChartCategory>>()
    val categories: LiveData<List<ChartCategory>> = _categories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        fetchChart()
    }

    private fun fetchChart() {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            repository.fetchDataChart()
                .collect{ result->
                    _isLoading.value = false
                    result.fold(
                        onSuccess = { response->
                            //_categories.value = mapResponseToDisplayableItems(response)
                            _outerListItems.value = mapResponseToOuterList(response)
                        },
                        onFailure = { exception ->
                            _error.value = "Failed to load  data: ${exception.localizedMessage}"
                            exception.printStackTrace()
                        }
                    )
                }
        }
    }

    private fun mapResponseToOuterList(response: DeezerChartResponse): List<OuterListItem> {
       // val resultCategories = mutableListOf<ChartCategory>()
        val resultList =  mutableListOf<OuterListItem>()
        val allAlbums = response.albums?.data

        //Square type value map track
        response.tracks?.data?.let { tracks ->
            if (tracks.isNotEmpty()) {
                val items = tracks.mapNotNull { track ->
                    if (track.title != null) DisplayableChartItem(
                        id = track.id, title = track.title,
                        imageUrl = track.album?.coverMedium, // Use album cover
                        secondaryInfo = track.artist?.name,
                        itemType = ChartItemType.TRACK, isCircle = false
                    ) else null
                }
                if (items.isNotEmpty()) {
                    resultList.add(OuterListItem.CategoryItem(ChartCategory("Top Tracks", items)))

                    val bannerAlbum = allAlbums?.firstOrNull()
                    val bannerImageUrl: String? = bannerAlbum?.coverMedium // Get URL or null
                    val bannerId: Long = bannerAlbum?.id ?: -1L // Get ID or use -1 as placeholder

                    // ALWAYS add the banner item, passing null for imageUrl if not found
                    resultList.add(OuterListItem.BannerAdItem(
                        id = bannerId,
                        imageUrl = bannerImageUrl // Will be null if no album image found
                    ))
                    Log.d("ChartDebug", "Added Standalone Banner item row. API ImageUrl: $bannerImageUrl")
                    // *** END BANNER ITEM ***

                } else {
                    Log.w("ChartDebug", "No valid artists mapped, skipping category and banner.")
                }
            }
        }

        //Square type value map album
        response.albums?.data?.let { albums->
            if (albums.isNotEmpty()){
                val items = albums.mapNotNull { album->
                    if (album.title!=null) DisplayableChartItem(
                        id = album.id, title = album.title,
                        imageUrl = album.coverMedium,
                        secondaryInfo = album.artist?.name,
                        itemType = ChartItemType.ALBUM, isCircle = false
                    ) else null
                }
                if (items.isNotEmpty()) {
                    resultList.add(OuterListItem.CategoryItem(ChartCategory("Top Tracks", items)))

                    val bannerAlbum = allAlbums?.firstOrNull()
                    val bannerImageUrl: String? = bannerAlbum?.coverMedium // Get URL or null
                    val bannerId: Long = bannerAlbum?.id ?: -1L // Get ID or use -1 as placeholder

                    // ALWAYS add the banner item, passing null for imageUrl if not found
                    resultList.add(OuterListItem.BannerAdItem(
                        id = bannerId,
                        imageUrl = bannerImageUrl // Will be null if no album image found
                    ))
                    Log.d("ChartDebug", "Added Standalone Banner item row. API ImageUrl: $bannerImageUrl")
                    // *** END BANNER ITEM ***

                } else {
                    Log.w("ChartDebug", "No valid artists mapped, skipping category and banner.")
                }
            }
        }

        //Circle type value map artists
        response.artists?.data?.let { artists->
            Log.d("ChartDebug", "API Artists Count: ${artists.size}")
            if (artists.isNotEmpty()){
                val items = artists.mapNotNull { artist ->
                    if (artist.name!=null) DisplayableChartItem(
                        id = artist.id, title = artist.name,
                        imageUrl = artist.pictureMedium,
                        secondaryInfo = artist.name,
                        itemType = ChartItemType.ARTIST, isCircle = true
                    ) else null
                }
                if (items.isNotEmpty()){
                    resultList.add(OuterListItem.CategoryItem(ChartCategory("Top Popular Artists", items)))

                    val bannerAlbum = allAlbums?.firstOrNull()
                    val bannerImageUrl: String? = bannerAlbum?.coverMedium // Get URL or null
                    val bannerId: Long = bannerAlbum?.id ?: -1L // Get ID or use -1 as placeholder

                    // ALWAYS add the banner item, passing null for imageUrl if not found
                    resultList.add(OuterListItem.BannerAdItem(
                        id = bannerId,
                        imageUrl = bannerImageUrl // Will be null if no album image found
                    ))
                    Log.d("ChartDebug", "Added Standalone Banner item row. API ImageUrl: $bannerImageUrl")
                    // *** END BANNER ITEM ***

                } else {
                    Log.w("ChartDebug", "No valid artists mapped, skipping category and banner.")
                }
            }
        }

        //Square type value map track
        response.tracks?.data?.let { tracks ->
            if (tracks.isNotEmpty()) {
                val items = tracks.mapNotNull { track ->
                    if (track.title != null) DisplayableChartItem(
                        id = track.id, title = track.title,
                        imageUrl = track.album?.coverMedium, // Use album cover
                        secondaryInfo = track.artist?.name,
                        itemType = ChartItemType.TRACK, isCircle = false
                    ) else null
                }
                if (items.isNotEmpty()) {
                    resultList.add(OuterListItem.CategoryItem(ChartCategory("Top Tracks", items)))

                    val bannerAlbum = allAlbums?.firstOrNull()
                    val bannerImageUrl: String? = bannerAlbum?.coverMedium // Get URL or null
                    val bannerId: Long = bannerAlbum?.id ?: -1L // Get ID or use -1 as placeholder

                    // ALWAYS add the banner item, passing null for imageUrl if not found
                    resultList.add(OuterListItem.BannerAdItem(
                        id = bannerId,
                        imageUrl = bannerImageUrl // Will be null if no album image found
                    ))
                    Log.d("ChartDebug", "Added Standalone Banner item row. API ImageUrl: $bannerImageUrl")
                    // *** END BANNER ITEM ***

                } else {
                    Log.w("ChartDebug", "No valid artists mapped, skipping category and banner.")
                }
            }
        }

        //Square type value map album
        response.albums?.data?.let { albums->
            if (albums.isNotEmpty()){
                val items = albums.mapNotNull { album->
                    if (album.title!=null) DisplayableChartItem(
                        id = album.id, title = album.title,
                        imageUrl = album.coverMedium,
                        secondaryInfo = album.artist?.name,
                        itemType = ChartItemType.ALBUM, isCircle = false
                    ) else null
                }
                if (items.isNotEmpty()) {
                    resultList.add(OuterListItem.CategoryItem(ChartCategory("Top Tracks", items)))

                    val bannerAlbum = allAlbums?.firstOrNull()
                    val bannerImageUrl: String? = bannerAlbum?.coverMedium // Get URL or null
                    val bannerId: Long = bannerAlbum?.id ?: -1L // Get ID or use -1 as placeholder

                    // ALWAYS add the banner item, passing null for imageUrl if not found
                    resultList.add(OuterListItem.BannerAdItem(
                        id = bannerId,
                        imageUrl = bannerImageUrl // Will be null if no album image found
                    ))
                    Log.d("ChartDebug", "Added Standalone Banner item row. API ImageUrl: $bannerImageUrl")
                    // *** END BANNER ITEM ***

                } else {
                    Log.w("ChartDebug", "No valid artists mapped, skipping category and banner.")
                }
            }
        }

        //Circle type value map artists
        response.artists?.data?.let { artists->
            Log.d("ChartDebug", "API Artists Count: ${artists.size}")
            if (artists.isNotEmpty()){
                val items = artists.mapNotNull { artist ->
                    if (artist.name!=null) DisplayableChartItem(
                        id = artist.id, title = artist.name,
                        imageUrl = artist.pictureMedium,
                        secondaryInfo = artist.name,
                        itemType = ChartItemType.ARTIST, isCircle = true
                    ) else null
                }
                if (items.isNotEmpty()){
                    resultList.add(OuterListItem.CategoryItem(ChartCategory("Top Popular Artists", items)))

                    val bannerAlbum = allAlbums?.firstOrNull()
                    val bannerImageUrl: String? = bannerAlbum?.coverMedium // Get URL or null
                    val bannerId: Long = bannerAlbum?.id ?: -1L // Get ID or use -1 as placeholder

                    // ALWAYS add the banner item, passing null for imageUrl if not found
                    resultList.add(OuterListItem.BannerAdItem(
                        id = bannerId,
                        imageUrl = bannerImageUrl // Will be null if no album image found
                    ))
                    Log.d("ChartDebug", "Added Standalone Banner item row. API ImageUrl: $bannerImageUrl")
                    // *** END BANNER ITEM ***

                } else {
                    Log.w("ChartDebug", "No valid artists mapped, skipping category and banner.")
                }
            }
        }
        return resultList
    }

    fun errorShow(){
        _error.value = null
    }
}
package com.example.caraousel.model

enum class ChartItemType{
    TRACK, ALBUM, ARTIST
}


data class DisplayableChartItem(
    val id: Long,
    val title: String,
    val imageUrl: String?,
    val secondaryInfo: String?,
    val itemType: ChartItemType,
    val isCircle: Boolean
)

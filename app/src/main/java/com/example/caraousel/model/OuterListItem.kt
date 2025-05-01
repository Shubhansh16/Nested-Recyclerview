package com.example.caraousel.model

import com.example.caraousel.viewmodel.ChartCategory

sealed class OuterListItem {

    data class CategoryItem(
        val category: ChartCategory
    ): OuterListItem()

    data class BannerAdItem(
        val id:Long,
        val imageUrl:String?
    ):OuterListItem()
}
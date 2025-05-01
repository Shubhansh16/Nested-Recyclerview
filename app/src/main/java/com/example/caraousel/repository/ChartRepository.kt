package com.example.caraousel.repository

import com.example.caraousel.model.DeezerChartResponse
import kotlinx.coroutines.flow.Flow

interface ChartRepository {
    fun fetchDataChart(): Flow<Result<DeezerChartResponse>>
}
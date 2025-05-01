package com.example.caraousel.repository

import com.example.caraousel.model.DeezerChartResponse
import com.example.caraousel.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

class ChartRepositoryImpl(private val apiService: ApiService):ChartRepository {
    override fun fetchDataChart(): Flow<Result<DeezerChartResponse>> = flow {
        val response = apiService.getChart()
        if (response!=null){
            emit(Result.success(response))
        } else {
            emit(Result.failure(IOException("API error: Failed to fetch data")))
        }
    }.catch { e->
        emit(Result.failure(e))
    }.flowOn(Dispatchers.IO)

}
package com.zef.wipro.repository

import androidx.lifecycle.LiveData
import com.zef.wipro.adapter.LoadingStatus
import com.zef.wipro.request.response.Facts

interface FactsRepository {
    fun fetchData(forceRefresh: Boolean)
    val factsLiveData: LiveData<Facts>
    val dataLoadingStatusLiveData:  LiveData<LoadingStatus>
}
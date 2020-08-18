package com.zef.wipro.repository

import androidx.lifecycle.LiveData
import com.zef.wipro.constants.LoadingStatus
import com.zef.wipro.constants.OperationCallback
import com.zef.wipro.request.response.Facts

/**
 * Facts Repository acts as a single source for Facts Data,
 */
interface FactsRepository {
    fun fetchData(callback: OperationCallback<Facts>,forceRefresh: Boolean)
    val factsLiveData: LiveData<Facts>
    val dataLoadingStatusLiveData:  LiveData<LoadingStatus>
}
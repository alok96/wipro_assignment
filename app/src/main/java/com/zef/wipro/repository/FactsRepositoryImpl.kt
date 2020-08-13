package com.zef.wipro.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zef.wipro.constants.LoadingStatus
import com.zef.wipro.db.FactsDao
import com.zef.wipro.request.FactsAPI
import com.zef.wipro.request.response.Facts
import com.zef.wipro.utils.ConnectivityInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Implementation of FactsRepository, Also persist the Facts data locally
 */
class FactsRepositoryImpl private constructor(
    private val context: Context,
    private val factsAPIService: FactsAPI,
    private val factsDao: FactsDao
) : FactsRepository,
    Callback<Facts?> {
    private val dataLoadingStatus: MutableLiveData<LoadingStatus>

    /**
     * Data can be fetched from remote or local cache.. as required
     *
     * @param forceRefresh force fetch data from remote if this is true
     */
    override fun fetchData(forceRefresh: Boolean) {
        dataLoadingStatus.value = LoadingStatus.LOADING
        if (forceRefresh || factsDao.facts.value == null) { // fetch data from remote
            fetchRemoteData()
        } else {
            fetchLocalData()
        }
    }

    override val factsLiveData: LiveData<Facts>
        get() = factsDao.facts

    override val dataLoadingStatusLiveData: LiveData<LoadingStatus>
        get() = dataLoadingStatus

    private fun fetchRemoteData() {
        if (ConnectivityInfo.isOnline(context)) {
            factsAPIService.factsData?.enqueue(this)
        } else {
            dataLoadingStatus.setValue(LoadingStatus.OFFLINE)
        }
    }

    private fun fetchLocalData() {
        // reset the same value
        val facts = factsDao.facts.value
        factsDao.setFacts(facts!!)
        dataLoadingStatus.value = LoadingStatus.FINISHED
    }

    override fun onResponse(call: Call<Facts?>, response: Response<Facts?>) {
        // persist data to FactsDao this will also update the observer
        factsDao.setFacts(response.body()!!)
        dataLoadingStatus.value = LoadingStatus.FINISHED
    }

    override fun onFailure(call: Call<Facts?>, t: Throwable) {
        dataLoadingStatus.value = LoadingStatus.ERROR
        Log.e("DataRepository", "onFailure error " + t.message)
    }

    companion object {
        private var instance: FactsRepository? = null

        @Synchronized
        fun getInstance(
            context: Context,
            apiService: FactsAPI,
            factsDao: FactsDao
        ): FactsRepository? {
            if (instance == null) {
                instance = FactsRepositoryImpl(context, apiService, factsDao)
            }
            return instance
        }
    }

    init {
        dataLoadingStatus = MutableLiveData(LoadingStatus.IDLE)
    }
}
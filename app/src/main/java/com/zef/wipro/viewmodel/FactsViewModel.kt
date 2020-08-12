package com.zef.wipro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zef.wipro.constants.LoadingStatus
import com.zef.wipro.repository.FactsRepository
import com.zef.wipro.request.response.Facts

class FactsViewModel internal constructor(private val factsRepository: FactsRepository) :
    ViewModel() {

    val factsLiveData: MutableLiveData<Facts>
        get() = factsRepository.factsLiveData as MutableLiveData<Facts>

    fun fetchData() {
        factsRepository.fetchData(false)
    }


    val dataLoadingStatusLiveData: LiveData<LoadingStatus>
        get() = factsRepository.dataLoadingStatusLiveData

    fun refresh() {
        factsRepository.fetchData(true)
    }
}
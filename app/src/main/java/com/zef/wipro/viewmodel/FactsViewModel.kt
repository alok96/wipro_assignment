package com.zef.wipro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zef.wipro.constants.LoadingStatus
import com.zef.wipro.constants.OperationCallback
import com.zef.wipro.data.Rows
import com.zef.wipro.repository.FactsRepository
import com.zef.wipro.request.response.Facts

class FactsViewModel internal constructor(private val factsRepository: FactsRepository) :
    ViewModel() {

    val factsLiveData: MutableLiveData<Facts>
        get() = factsRepository.factsLiveData as MutableLiveData<Facts>

    private val _facts = MutableLiveData<List<Facts>>().apply { value = emptyList() }
    val facts: LiveData<List<Facts>> = _facts

    private val _isViewLoading = MutableLiveData<Boolean>()
    val isViewLoading: LiveData<Boolean> = _isViewLoading

    private val _onMessageError = MutableLiveData<Any>()
    val onMessageError: LiveData<Any> = _onMessageError

    private val _isEmptyList = MutableLiveData<Boolean>()
    val isEmptyList: LiveData<Boolean> = _isEmptyList

    fun fetchData() {
        _isViewLoading.postValue(true)
        factsRepository.fetchData(object : OperationCallback<Facts> {
            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(error)
            }

            override fun onSuccess(data: List<Facts>?) {
                _isViewLoading.postValue(false)

                if (data != null) {
                    if (data.isEmpty()) {
                        _isEmptyList.postValue(true)
                    } else {
                        _facts.value = data
                    }
                }
            }
        }, false)
    }

    val dataLoadingStatusLiveData: LiveData<LoadingStatus>
        get() = factsRepository.dataLoadingStatusLiveData

    fun refresh() {
        factsRepository.fetchData(object : OperationCallback<Facts> {
            override fun onError(error: String?) {
                _isViewLoading.postValue(false)
                _onMessageError.postValue(error)
            }

            override fun onSuccess(data: List<Facts>?) {
                _isViewLoading.postValue(false)

                if (data != null) {
                    if (data.isEmpty()) {
                        _isEmptyList.postValue(true)
                    } else {
                        _facts.value = data
                    }
                }
            }
        }, true)
    }
}
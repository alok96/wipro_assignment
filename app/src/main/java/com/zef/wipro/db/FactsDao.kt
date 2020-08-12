package com.zef.wipro.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zef.wipro.request.response.Facts

class FactsDao {
    private val factsMutableLiveData: MutableLiveData<Facts>

    init {
        factsMutableLiveData = MutableLiveData()
    }

    fun setFacts(facts: Facts) {
        factsMutableLiveData.value = facts
    }

    val facts: LiveData<Facts>
        get() = factsMutableLiveData
}
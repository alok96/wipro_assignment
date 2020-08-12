package com.zef.wipro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zef.wipro.repository.FactsRepository

class FactsViewModelFactory(private val factsRepository: FactsRepository?) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return factsRepository?.let { FactsViewModel(it) } as T
    }
}
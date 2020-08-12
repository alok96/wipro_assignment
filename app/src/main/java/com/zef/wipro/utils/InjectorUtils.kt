package com.zef.wipro.utils

import android.content.Context
import com.zef.wipro.db.FactsDatabase
import com.zef.wipro.repository.FactsRepositoryImpl
import com.zef.wipro.request.ServiceGenerator
import com.zef.wipro.viewmodel.FactsViewModelFactory

/**
 * Manage all app level dependencies in this class
 */
class InjectorUtil private constructor(context: Context) {
    private val context: Context

    init {
        this.context = context.applicationContext
    }

    companion object {
        private var instance: InjectorUtil? = null

        @Synchronized
        fun getInstance(context: Context): InjectorUtil? {
            if (instance == null) {
                instance = InjectorUtil(context)
            }
            return instance
        }
    }

    fun provideFactsViewModelFactory(): FactsViewModelFactory {
        val apiService = ServiceGenerator.instance!!.factsAPI
        val factsDao = FactsDatabase.instance?.factsDao
        return FactsViewModelFactory(factsDao?.let {
            FactsRepositoryImpl.getInstance(
                context,
                apiService,
                it
            )
        })
    }
}
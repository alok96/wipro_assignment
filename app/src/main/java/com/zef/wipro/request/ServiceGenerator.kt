package com.zef.wipro.request

import com.zef.wipro.constants.ApiConstants
import com.zef.wipro.constants.ApiConstants.CONNECTION_TIMEOUT
import com.zef.wipro.constants.ApiConstants.READ_TIMEOUT
import com.zef.wipro.constants.ApiConstants.WRITE_TIMEOUT
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceGenerator {
    val factsAPI: FactsAPI

    companion object {
        var instance: ServiceGenerator? = null
            get() {
                if (field == null) {
                    field = ServiceGenerator()
                }
                return field
            }
            private set
        private val client = OkHttpClient.Builder() // establish connection to server
            .connectTimeout(
                CONNECTION_TIMEOUT.toLong(),
                TimeUnit.SECONDS
            ) // time between each byte read from the server
            .readTimeout(
                READ_TIMEOUT.toLong(),
                TimeUnit.SECONDS
            ) // time between each byte sent to server
            .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build()

    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        factsAPI = retrofit.create(FactsAPI::class.java)
    }
}
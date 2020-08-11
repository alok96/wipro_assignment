package com.zef.wipro.request

import com.zef.wipro.request.response.Facts
import retrofit2.Call
import retrofit2.http.GET

interface FactsAPI {
    @get:GET ("/s/2iodh4vg0eortkl/facts.json")
    val factsData: Call<Facts>
}
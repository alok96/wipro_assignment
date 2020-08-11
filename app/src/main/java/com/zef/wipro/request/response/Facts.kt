package com.zef.wipro.request.response

import android.icu.text.CaseMap
import com.google.gson.annotations.SerializedName
import com.zef.wipro.data.Rows

data class Facts(
    @SerializedName("rows") val rows: List<Rows>,
    @SerializedName("title") val title: String
)
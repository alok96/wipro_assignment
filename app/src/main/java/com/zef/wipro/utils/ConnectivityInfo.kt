package com.zef.wipro.utils

import android.content.Context
import android.net.ConnectivityManager

object ConnectivityInfo {
    fun isOnline(context: Context):Boolean{
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        return networkInfo!=null && networkInfo.isConnectedOrConnecting
    }
}
@file:Suppress("DEPRECATION")

package com.kcrason.appbasic.common.helper

import android.net.ConnectivityManager
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.NetworkInfo


object NetworkHelper {

    const val NETWORK_STATUS_WIFI = 0
    const val NETWORK_STATUS_MOBILE = 1
    const val NETWORK_STATUS_NONE = 2

    fun getNetWorkState(context: Context): Int {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            //wifi
            if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI)
                return NETWORK_STATUS_WIFI
            else if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                return NETWORK_STATUS_MOBILE//mobile
            }
        }
        return NETWORK_STATUS_NONE
    }


    fun isNetWorkAvailable(context: Context?): Boolean {
        context?.let {
            val connectivityManager: ConnectivityManager? =
                it.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? = connectivityManager?.activeNetworkInfo
            return networkInfo?.isConnected ?: false
        } ?: return true
    }
}
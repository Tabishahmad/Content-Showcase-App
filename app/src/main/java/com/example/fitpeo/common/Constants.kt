package com.example.fitpeo.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi

const val DATABASE_TABLE_NAME = "favourite_book_table"
const val ALBUM_OBJ = "album_obj"
const val MAX_PAGE = 2

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var isConnected: Boolean
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
       val network = connectivityManager.activeNetwork ?: return false
       val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
       isConnected = when {
           activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
           activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
           else -> false
       }
    } else {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        isConnected = networkInfo?.isConnectedOrConnecting ?: false
    }

    return isConnected
}
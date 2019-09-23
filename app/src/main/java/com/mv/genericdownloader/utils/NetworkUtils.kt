package com.mv.genericdownloader.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import com.mv.genericdownloader.R

object NetworkUtils {

    fun isNetworkConnected(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        if (context is Activity) {
            if (context.isFinishing) {
                return false
            }
        }
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.activeNetworkInfo
        }
        val flag = activeNetworkInfo != null && activeNetworkInfo.isConnected
        if (!flag) {
            Toast.makeText(context, R.string.nointernet, Toast.LENGTH_SHORT).show()
        }
        return flag
    }
}// This class is not publicly instantiable

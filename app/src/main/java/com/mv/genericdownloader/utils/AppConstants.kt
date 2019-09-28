package com.mv.genericdownloader.utils

import android.Manifest

class AppConstants {
    companion object {
        val CACHE_SIZE = (((Runtime.getRuntime().maxMemory() / 1024)) / 32).toInt()
        val PERMISSIONS = mutableListOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}

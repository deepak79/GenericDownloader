package com.mv.genericdownloaderlib.utils

/***
 * Constants class
 */
class LibConstants {
    companion object {
        //Default cache size can be tweaked
        val DEFAULT_CACHE_SIZE = (((Runtime.getRuntime().maxMemory() / 1024)) / 32).toInt()
    }
}
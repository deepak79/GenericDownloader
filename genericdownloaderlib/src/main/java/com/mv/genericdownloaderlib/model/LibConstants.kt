package com.mv.genericdownloaderlib.model

class LibConstants {
    companion object {
        val DEFAULT_CACHE_SIZE = (((Runtime.getRuntime().maxMemory() / 1024)) / 8).toInt()
    }
}
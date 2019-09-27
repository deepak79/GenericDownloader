package com.mv.genericdownloaderlib.model

/**
 * StringResource to download string using GenericDownloadManager
 *
 * */
class StringResource(private val response: Any) : BaseResource() {

    /**
     * To convert response to string
     * @return response as string
     * */
    @Throws(Exception::class)
    fun getString(): String? {
        return response as String
    }
}
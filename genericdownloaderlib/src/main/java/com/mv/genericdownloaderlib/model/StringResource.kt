package com.mv.genericdownloaderlib.model

class StringResource(private val response: Any) : BaseResource() {
    @Throws(Exception::class)
    fun getString(): String? {
        return response as String
    }
}
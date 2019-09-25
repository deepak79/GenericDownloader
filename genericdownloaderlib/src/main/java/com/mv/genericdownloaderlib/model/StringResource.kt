package com.mv.genericdownloaderlib.model

import com.mv.genericdownloaderlib.utils.Base64
import java.io.IOException

class StringResource(private val byteArray: ByteArray) : BaseResource() {
    @Throws(Exception::class)
    fun getString(): String? {
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
}
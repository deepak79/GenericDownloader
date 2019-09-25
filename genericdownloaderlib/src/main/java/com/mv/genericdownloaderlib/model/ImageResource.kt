package com.mv.genericdownloaderlib.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory

open class ImageResource(private val byteArray: ByteArray) : BaseResource() {
    @Throws(Exception::class)
    fun getBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
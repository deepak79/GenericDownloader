package com.mv.genericdownloaderlib.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

open class ImageResource(private val response: Any) : BaseResource() {
    @Throws(Exception::class)
    fun getBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(response as ByteArray, 0, response.size)
    }
}
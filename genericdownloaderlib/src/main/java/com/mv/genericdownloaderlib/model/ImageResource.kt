package com.mv.genericdownloaderlib.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.InputStream

open class ImageResource(private val inputStream: InputStream) : BaseResource() {

    fun getBitmap(): Bitmap {
        return BitmapFactory.decodeStream(inputStream)
    }
}
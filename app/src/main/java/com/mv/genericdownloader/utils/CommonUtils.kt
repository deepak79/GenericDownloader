package com.mv.genericdownloader.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast


object CommonUtils {
    fun makeText(context: Context?, message: String) {
        if (context == null) {
            return
        }
        if (context is Activity) {
            if (context.isFinishing) {
                return
            }
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

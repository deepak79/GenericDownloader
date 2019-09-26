package com.mv.genericdownloaderlib.model

import org.json.JSONArray
import org.json.JSONObject


class JSONResource(private val response: Any) : BaseResource() {

    @Throws(Exception::class)
    fun getAsJSONObject(): JSONObject? {
        return JSONObject(response as String)
    }

    @Throws(Exception::class)
    fun getAsJSONArray(): JSONArray? {
        return JSONArray(response as String)
    }
}
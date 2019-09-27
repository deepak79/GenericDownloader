package com.mv.genericdownloaderlib.model

import org.json.JSONArray
import org.json.JSONObject

/**
 * JSONResource to download JSON using GenericDownloadManager
 *
 * */
class JSONResource(private val response: Any) : BaseResource() {
    /**
     * To convert response to JSONObject
     * @return response as JSONObject
     * */
    @Throws(Exception::class)
    fun getAsJSONObject(): JSONObject? {
        return JSONObject(response as String)
    }
    /**
     * To convert response to JSONArray
     * @return response as JSONArray
     * */
    @Throws(Exception::class)
    fun getAsJSONArray(): JSONArray? {
        return JSONArray(response as String)
    }
}
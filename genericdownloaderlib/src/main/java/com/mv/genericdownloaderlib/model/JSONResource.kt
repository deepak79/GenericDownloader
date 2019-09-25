package com.mv.genericdownloaderlib.model

import com.google.gson.Gson
import com.mv.genericdownloaderlib.utils.Base64
import org.json.JSONArray
import org.json.JSONObject


class JSONResource(private val byteArray: ByteArray) : BaseResource() {
    companion object {
        val gson = Gson()
    }
    @Throws(Exception::class)
    fun getAsJSONObject(): JSONObject? {
        return JSONObject(gson.toJson(Base64.encodeToString(byteArray, Base64.NO_WRAP)))
    }
    @Throws(Exception::class)
    fun getAsJSONArray(): JSONArray? {
        return JSONArray(gson.toJson(Base64.encodeToString(byteArray, Base64.NO_WRAP)))
    }
    @Throws(Exception::class)
    fun getAsJSONString(): String? {
        return gson.toJson(Base64.encodeToString(byteArray, Base64.NO_WRAP))
    }
}
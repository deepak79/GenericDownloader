package com.mv.genericdownloaderlib.interfaces

import com.mv.genericdownloaderlib.model.BaseResource

/**
 * Interface for resource request callback
 * */
interface IResourceRequestCallBack<T : BaseResource> {
    fun onSuccess(data: T)

    fun onFailure(error: String?)
}
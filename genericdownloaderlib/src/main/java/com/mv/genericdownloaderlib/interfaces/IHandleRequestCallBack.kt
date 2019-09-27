package com.mv.genericdownloaderlib.interfaces

/**
 * Interface for handle requested resource callback
 * */
interface IHandleRequestCallBack {
    fun onCancel()
    fun onRetry()
}
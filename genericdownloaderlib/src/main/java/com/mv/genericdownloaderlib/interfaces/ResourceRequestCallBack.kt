package com.mv.genericdownloaderlib.interfaces

import com.mv.genericdownloaderlib.core.GenericDownloadManager

interface ResourceRequestCallBack {
    fun onStart(genericDownloadManager: GenericDownloadManager)

    fun onSuccess(genericDownloadManager: GenericDownloadManager)

    fun onFailure(genericDownloadManager: GenericDownloadManager)

    fun onRetry(genericDownloadManager: GenericDownloadManager)
}
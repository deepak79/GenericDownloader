package com.mv.genericdownloaderlib.interfaces

import io.reactivex.Observable
import java.io.InputStream

interface IRemoteCallManger {
    fun fetchRemoteData(mURL: String): Observable<InputStream>
}
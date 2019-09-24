package com.mv.genericdownloaderlib.interfaces

import io.reactivex.Observable
import java.io.InputStream

interface ICacheManger {

    fun fetchCachedData(mURL: String): Observable<InputStream>
}
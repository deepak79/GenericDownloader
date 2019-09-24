package com.mv.genericdownloaderlib.core

import com.mv.genericdownloaderlib.cache.LRUCache
import com.mv.genericdownloaderlib.interfaces.IDataManager
import com.mv.genericdownloaderlib.interfaces.IHandleRequestCallBack
import com.mv.genericdownloaderlib.interfaces.IResourceRequestCallBack
import com.mv.genericdownloaderlib.model.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest


class GenericDownloadManager(
    private val mResourceURL: String,
    private val mResourceTypes: ResourceTypes,
    private val mIResourceRequestCallBack: IResourceRequestCallBack<BaseResource>
): IDataManager, IHandleRequestCallBack {
    private lateinit var disposable: Disposable
    private val client = OkHttpClient()

    companion object {
        private val mCacheSize = (((Runtime.getRuntime().maxMemory() / 1024)) / 8).toInt()
        val mLruCache = LRUCache(mCacheSize)
    }

    init {
        getData()
    }

    private fun getObserver(): DisposableObserver<InputStream> {
        return object : DisposableObserver<InputStream>() {
            override fun onComplete() {
            }

            override fun onNext(t: InputStream) {
                mLruCache.set(getMD5EncryptedString(mResourceURL), t)
                when (mResourceTypes) {
                    ResourceTypes.IMAGE -> mIResourceRequestCallBack.onSuccess(ImageResource(t))
                    ResourceTypes.JSON -> mIResourceRequestCallBack.onSuccess(JSONResource(t))
                    ResourceTypes.STRING -> mIResourceRequestCallBack.onSuccess(StringResource(t))
                }
            }

            override fun onError(e: Throwable) {
                mIResourceRequestCallBack.onFailure(e.message)
            }
        }
    }

    private fun getData() {
        val cache = fetchCachedData(mResourceURL)
        val remote = fetchRemoteData(mResourceURL)
        disposable = Observable.concat(cache, remote)
            .firstElement()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .subscribeWith(getObserver())
    }

    @Throws(Exception::class)
    private fun getMD5EncryptedString(encTarget: String): String {
        val mdEnc: MessageDigest? = MessageDigest.getInstance("MD5")
        mdEnc!!.update(encTarget.toByteArray(), 0, encTarget.length)
        var md5 = BigInteger(1, mdEnc.digest()).toString(16)
        while (md5.length < 32) {
            md5 = "0$md5"
        }
        return md5
    }

    override fun fetchCachedData(mURL: String): Observable<InputStream> {
        return Observable.create { emitter ->
            if (mLruCache.contains(getMD5EncryptedString(mURL))) {
                emitter.onNext(mLruCache.get(getMD5EncryptedString(mURL)) as InputStream)
            }
            emitter.onComplete()
        }
    }

    override fun fetchRemoteData(mURL: String): Observable<InputStream> {
        return Observable.create { emitter ->
            val request = Request.Builder()
                .url(mURL)
                .build()
            val response = client.newCall(request).execute()
            emitter.onNext(response.body?.byteStream()!!)
            emitter.onComplete()
        }
    }


    override fun onCancel() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    override fun onRetry() {
        getData()
    }
}
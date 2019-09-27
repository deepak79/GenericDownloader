package com.mv.genericdownloaderlib.core

import com.mv.genericdownloaderlib.cache.LRUCache
import com.mv.genericdownloaderlib.enums.ResourceTypes
import com.mv.genericdownloaderlib.interfaces.IHandleRequestCallBack
import com.mv.genericdownloaderlib.interfaces.IResourceRequestCallBack
import com.mv.genericdownloaderlib.model.BaseResource
import com.mv.genericdownloaderlib.model.ImageResource
import com.mv.genericdownloaderlib.model.JSONResource
import com.mv.genericdownloaderlib.model.StringResource
import com.mv.genericdownloaderlib.utils.LibConstants.Companion.DEFAULT_CACHE_SIZE
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Core class GenericDownloadManager to download different type of resources from internet
 * @param mResourceURL an URL to request resource to be downloaded
 * @param mResourceTypes type of resource user is requesting
 * @param mIResourceRequestCallBack interface for resource request callback
 * */
class GenericDownloadManager(
    private val mResourceURL: String,
    private val mResourceTypes: ResourceTypes,
    private val mIResourceRequestCallBack: IResourceRequestCallBack<BaseResource>
) : IHandleRequestCallBack {
    /**
     * disposable to add observable to support cancel request feature
     */
    private lateinit var disposable: Disposable

    companion object {
        /**
         * instance of OkHttpClient to handle network request
         */
        private val client = OkHttpClient()
        /**
         * instance of LruCache to handle caching mechanism
         */
        private val mLruCache = LRUCache(DEFAULT_CACHE_SIZE)

        /**
         * To clear the cached items
         */
        fun clearCache() {
            mLruCache.clear()
        }

        /**
         * To convert inputstreamt to byte array
         */
        @Throws(IOException::class)
        fun readAllBytes(ins: InputStream): ByteArray {
            val bufLen = 4 * 0x400 // 4KB
            val buf = ByteArray(bufLen)
            var readLen: Int = 0

            ByteArrayOutputStream().use { o ->
                ins.use { i ->
                    while (i.read(buf, 0, bufLen).also { readLen = it } != -1)
                        o.write(buf, 0, readLen)
                }

                return o.toByteArray()
            }
        }

        /**
         * To get md5 of string to avoid reloading same resource
         */
        @Throws(Exception::class)
        fun getMD5EncryptedString(encTarget: String): String {
            val mdEnc: MessageDigest? = MessageDigest.getInstance("MD5")
            mdEnc!!.update(encTarget.toByteArray(), 0, encTarget.length)
            var md5 = BigInteger(1, mdEnc.digest()).toString(16)
            while (md5.length < 32) {
                md5 = "0$md5"
            }
            return md5
        }
    }

    init {
        getData()
    }

    /**
     * Observer to observe data returned by Network call or cache
     */
    private fun getObserver(): DisposableObserver<Any> {
        return object : DisposableObserver<Any>() {
            override fun onComplete() {
            }

            override fun onNext(inpStream: Any) {
                if (!mLruCache.contains(getMD5EncryptedString(mResourceURL))) {
                    mLruCache.set(getMD5EncryptedString(mResourceURL), inpStream)
                }
                when (mResourceTypes) {
                    ResourceTypes.IMAGE -> mIResourceRequestCallBack.onSuccess(
                        ImageResource(
                            inpStream
                        )
                    )
                    ResourceTypes.JSON -> mIResourceRequestCallBack.onSuccess(
                        JSONResource(
                            inpStream
                        )
                    )
                    ResourceTypes.STRING -> mIResourceRequestCallBack.onSuccess(
                        StringResource(
                            inpStream
                        )
                    )
                }
            }

            override fun onError(e: Throwable) {
                mIResourceRequestCallBack.onFailure(e.message)
            }
        }
    }

    /**
     * To get data either from cache or network call
     */
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

    /**
     * @param mURL URL of requested resource
     *
     * @return data return from cache
     * */
    private fun fetchCachedData(mURL: String): Observable<Any> {
        return Observable.create { emitter ->
            if (mLruCache.contains(getMD5EncryptedString(mURL))) {
                emitter.onNext(mLruCache.get(getMD5EncryptedString(mURL)) as ByteArray)
            }
            emitter.onComplete()
        }
    }
    /**
     * @param mURL URL of requested resource
     *
     * @return data return from network call
     * */
    private fun fetchRemoteData(mURL: String): Observable<Any> {
        return Observable.create { emitter ->
            val request = Request.Builder()
                .url(mURL)
                .build()
            val response = client.newCall(request).execute()
            try {
                when (mResourceTypes) {
                    ResourceTypes.IMAGE -> {
                        emitter.onNext(readAllBytes(response.body?.byteStream()!!))
                    }
                    ResourceTypes.STRING,
                    ResourceTypes.JSON -> {
                        try {
                            emitter.onNext(response.body?.string()!!)
                        } catch (e: Exception) {
                            emitter.onError(java.lang.Exception(e))
                        }
                    }
                }
            } catch (e: java.lang.Exception) {
                emitter.onError(e)
            } finally {
                response.body?.close()
                emitter.onComplete()
            }
        }
    }
    /**
     * To cancel the ongoing request to download resource
     * */
    override fun onCancel() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    /**
     * To retry the request
     * */
    override fun onRetry() {
        getData()
    }
}
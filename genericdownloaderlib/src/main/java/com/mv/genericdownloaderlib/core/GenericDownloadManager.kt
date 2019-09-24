package com.mv.genericdownloaderlib.core

import com.mv.genericdownloaderlib.cache.LRUCache
import com.mv.genericdownloaderlib.interfaces.IHandleRequestCallBack
import com.mv.genericdownloaderlib.interfaces.IResourceRequestCallBack
import com.mv.genericdownloaderlib.model.*
import com.mv.genericdownloaderlib.model.LibConstants.Companion.DEFAULT_CACHE_SIZE
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
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
) : IHandleRequestCallBack {
    private lateinit var disposable: Disposable
    private val client = OkHttpClient()

    companion object {
        private val mLruCache = LRUCache(DEFAULT_CACHE_SIZE)

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

    private fun getObserver(): DisposableObserver<InputStream> {
        return object : DisposableObserver<InputStream>() {
            override fun onComplete() {
            }

            override fun onNext(inpStream: InputStream) {
                if (inpStream != null) {
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
            }

            override fun onError(e: Throwable) {
                mIResourceRequestCallBack.onFailure(e.message)
            }
        }
    }

    private fun getData() {
        val cache = fetchCachedData(mResourceURL)
        if (cache == null) {
            Observable.create(ObservableOnSubscribe<InputStream?> { emitter ->
                try {
                    emitter.onNext(fetchRemoteData(mResourceURL))
                    emitter.onComplete()
                } catch (e: java.lang.Exception) {
                    emitter.onError(e)
                }
            }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver())
        } else {
            getObserver().onNext(cache)
            getObserver().onComplete()
        }
    }


    private fun fetchCachedData(mURL: String): InputStream? {
        if (mLruCache.contains(getMD5EncryptedString(mURL))) {
            return mLruCache.get(getMD5EncryptedString(mURL)) as InputStream
        } else {
            return null
        }
    }

    private fun fetchRemoteData(mURL: String): InputStream {
        val request = Request.Builder()
            .url(mURL)
            .build()
        val response = client.newCall(request).execute()
        return response.body?.byteStream()!!
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
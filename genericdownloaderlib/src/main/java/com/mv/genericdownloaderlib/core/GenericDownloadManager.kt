package com.mv.genericdownloaderlib.core

import com.mv.genericdownloaderlib.cache.Cache
import com.mv.genericdownloaderlib.model.ResourceType
import java.math.BigInteger
import java.security.MessageDigest

abstract class GenericDownloadManager : Cache {
    private var mResourceURL: String? = null
    private var mResource: ByteArray? = null
    private var mResourceType: ResourceType? = null


    @Throws(Exception::class)
    private fun getMD5EncryptedString(encTarget: String): String {
        var mdEnc: MessageDigest? = null
        mdEnc = MessageDigest.getInstance("MD5")
        // Encryption algorithm
        mdEnc!!.update(encTarget.toByteArray(), 0, encTarget.length)
        var md5 = BigInteger(1, mdEnc.digest()).toString(16)
        while (md5.length < 32) {
            md5 = "0$md5"
        }
        return md5
    }

    override val size: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun set(key: Any, value: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun get(key: Any): Any? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(key: Any): Any? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
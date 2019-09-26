package com.mv.genericdownloader

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.test.runner.AndroidJUnit4
import com.mv.genericdownloaderlib.core.GenericDownloadManager
import com.mv.genericdownloaderlib.enums.ResourceTypes
import com.mv.genericdownloaderlib.interfaces.IResourceRequestCallBack
import com.mv.genericdownloaderlib.model.BaseResource
import com.mv.genericdownloaderlib.model.ImageResource
import com.mv.genericdownloaderlib.model.JSONResource
import okhttp3.internal.lockAndWaitNanos
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext =
            androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.mv.genericdownloaderlib.test", appContext.packageName)
    }

    @Test
    fun download_ImageResource_test_should_pass() {
        val appContext =
            androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext
        val url =
            "https://images.unsplash.com/profile-1464495186405-68089dcd96c3?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=faces\\u0026fit=crop\\u0026h=32\\u0026w=32\\u0026s=63f1d805cffccb834cf839c719d91702"
        val expected =
            ((appContext.resources.getDrawable(R.drawable.unit_test_img1)) as BitmapDrawable).bitmap
        var res: Bitmap? = null
        GenericDownloadManager(url,
            ResourceTypes.IMAGE, object : IResourceRequestCallBack<BaseResource> {
                override fun onSuccess(data: BaseResource) {
                    res = (data as ImageResource).getBitmap()
                }

                override fun onFailure(error: String?) {
                    fail("Failed with error $error")
                }
            })
        //wait for network response
        lockAndWaitNanos(2000000000)
        assertNotNull(expected)
        assertNotNull(res)
        assert(expected.sameAs(res))
        assertEquals("width", expected.width, res!!.width)
        assertEquals("rowbytes", expected.rowBytes, res!!.rowBytes)
        assertEquals("height", expected.height, res!!.height)
    }

    @Test
    fun download_ImageResource_test_should_fail() {
        val appContext =
            androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext
        val url =
            "https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-0.3.5\\u0026q=80\\u0026fm=jpg\\u0026crop=faces\\u0026fit=crop\\u0026h=32\\u0026w=32\\u0026s=46caf91cf1f90b8b5ab6621512f102a8"
        val expected =
            ((appContext.resources.getDrawable(R.drawable.unit_test_img1)) as BitmapDrawable).bitmap
        var res: Bitmap? = null
        GenericDownloadManager(url,
            ResourceTypes.IMAGE, object : IResourceRequestCallBack<BaseResource> {
                override fun onSuccess(data: BaseResource) {
                    res = (data as ImageResource).getBitmap()
                }

                override fun onFailure(error: String?) {
                    fail("Failed with error $error")
                }
            })
        //wait for network response
        lockAndWaitNanos(2000000000)
        assertNotNull(expected)
        assertNotNull(res)
        assert(expected.sameAs(res))
        assertEquals("width", expected.width, res!!.width)
        assertEquals("rowbytes", expected.rowBytes, res!!.rowBytes)
        assertEquals("height", expected.height, res!!.height)
    }

    @Test
    fun download_JSONResource_test_should_pass() {
        val url = "https://pastebin.com/Z60FvjbX"
        val expected = JSONObject("{\"squadName\":\"Super hero squad\",\"homeTown\":\"Metro City\",\"formed\":2016,\"secretBase\":\"Super tower\",\"active\":true}")
        var res: JSONObject? = null
        GenericDownloadManager(url,
            ResourceTypes.JSON, object : IResourceRequestCallBack<BaseResource> {
                override fun onSuccess(data: BaseResource) {
                    res = (data as JSONResource).getAsJSONObject()
                }

                override fun onFailure(error: String?) {
                    fail("Failed with error $error")
                }
            })
        //wait for network response
        lockAndWaitNanos(2000000000)
        assertNotNull(res)
        assertEquals(expected, res)
    }
}

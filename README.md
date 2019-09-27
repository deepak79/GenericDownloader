# GenericDownloader

Simple library to download different types of resources from network. Currently we support ```String, JSON and Bitmap```.
You can fork it and add different resources than this. Uses LruCache internally to avoid redownloading the same resource.

The resources we support now is:
```
ImageResource
JSONResource
StringResource
```

It simple to use:

Example to download image from URL

```kotlin
val URL = "https://images.unsplash.com/profile-1464495186405-68089dcd96c3?ixlib=rb-0.3.5\u0026q=80\u0026fm=jpg\u0026crop=faces\u0026fit=crop\u0026h=32\u0026w=32\u0026s=63f1d805cffccb834cf839c719d91702"
GenericDownloadManager(
    URL,
    ResourceTypes.IMAGE, object : IResourceRequestCallBack<BaseResource> {
        override fun onSuccess(data: BaseResource) {
            img.setImageBitmap((data as ImageResource).getBitmap())
        }

        override fun onFailure(error: String?) {
            Log.e("@@@@", "Failure $error")
        }
})
```

Example to download JSONObject from URL

```kotlin
val URL = "https://pastebin.com/raw/wgkJgazE"
GenericDownloadManager(
    URL,
    ResourceTypes.JSON, object : IResourceRequestCallBack<BaseResource> {
        override fun onSuccess(data: BaseResource) {
            res = (data as JSONResource).getAsJSONObject()
        }

        override fun onFailure(error: String?) {
            Log.e("@@@@", "Failure $error")
        }
})
```


Example to download JSONArray from URL

```kotlin
val URL = "https://pastebin.com/raw/wgkJgazE"
GenericDownloadManager(
    URL,
    ResourceTypes.JSON, object : IResourceRequestCallBack<BaseResource> {
        override fun onSuccess(data: BaseResource) {
            res = (data as JSONResource).getAsJSONArray()
        }

        override fun onFailure(error: String?) {
            Log.e("@@@@", "Failure $error")
        }
})
```

Example to download String from URL

```kotlin
val URL = "https://pastebin.com/raw/wgkJgazE"
GenericDownloadManager(
    URL,
    ResourceTypes.STRING, object : IResourceRequestCallBack<BaseResource> {
        override fun onSuccess(data: BaseResource) {
            res = (data as StringResource).getString()
        }

        override fun onFailure(error: String?) {
            Log.e("@@@@", "Failure $error")
        }
})
```

We can cancel the ongoing request with 
```kotlin
 mGenericDownloadManager.onCancel()
```

We can cancel the retry request with 
```kotlin
 mGenericDownloadManager.onRetry()
```

Unit tests are also included
```kotlin
@Test
    fun download_JSONResource_test_should_pass() {
        val url = "https://pastebin.com/raw/Z60FvjbX"
        val expected =
            JSONObject("{\"squadName\":\"Super hero squad\",\"homeTown\":\"Metro City\",\"formed\":2016,\"secretBase\":\"Super tower\",\"active\":true}")
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
        assertEquals(expected.toString(), res.toString())
    }
```

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

```
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

```
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

```
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

```
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
```
 mGenericDownloadManager.onCancel()
```

We can cancel the retry request with 
```
 mGenericDownloadManager.onRetry()
```

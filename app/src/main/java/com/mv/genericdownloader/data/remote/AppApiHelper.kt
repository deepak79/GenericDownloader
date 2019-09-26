package com.mv.genericdownloader.data.remote

import com.androidnetworking.common.Priority
import com.mv.genericdownloader.model.response.DataResponse
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppApiHelper @Inject
constructor(
) : ApiHelper {

    override fun getData(): Single<MutableList<DataResponse>> {
        return Rx2AndroidNetworking.get(ApiEndPoint.ENDPOINT_GET_DATA_)
            .setPriority(Priority.HIGH)
            .build()
            .getObjectListSingle(DataResponse::class.java)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
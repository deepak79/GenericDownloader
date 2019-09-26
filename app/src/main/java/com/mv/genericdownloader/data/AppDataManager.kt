package com.mv.genericdownloader.data

import com.mv.genericdownloader.data.remote.ApiHelper
import com.mv.genericdownloader.model.response.DataResponse
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager @Inject
constructor(
    private val mApiHelper: ApiHelper
) : DataManager {

    override fun getData(): Single<MutableList<DataResponse>> {
        return mApiHelper.getData()
    }
}

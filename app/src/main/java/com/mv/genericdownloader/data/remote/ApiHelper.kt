package com.mv.genericdownloader.data.remote

import com.mv.genericdownloader.model.response.DataResponse
import io.reactivex.Single

interface ApiHelper {
    fun getData(): Single<List<DataResponse>>
}


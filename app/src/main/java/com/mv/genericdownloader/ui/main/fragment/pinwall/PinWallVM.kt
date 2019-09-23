package com.mv.genericdownloader.ui.main.fragment.pinwall

import androidx.lifecycle.MutableLiveData
import com.mv.genericdownloader.data.DataManager
import com.mv.genericdownloader.model.response.DataResponse
import com.mv.genericdownloader.ui.base.BaseViewModel
import com.mv.genericdownloader.utils.rx.SchedulerProvider


class PinWallVM(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<PinWallNavigator>(dataManager, schedulerProvider) {
    private var mDataResponse: MutableLiveData<DataResponse> =
        MutableLiveData()

    private fun handleError(error: Throwable?) {
        setIsLoading(false)
        getNavigator().onHandleError(error?.message!!)
    }

    fun getDataResponseLiveData(): MutableLiveData<DataResponse> {
        return mDataResponse
    }

    fun getData() {
        setIsLoading(true)
        compositeDisposable.add(
            dataManager.getData()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ data ->
                    setIsLoading(false)
                    mDataResponse.postValue(data)
                }, { throwable ->
                    handleError(throwable)
                    mDataResponse.postValue(null)
                })
        )
    }
}
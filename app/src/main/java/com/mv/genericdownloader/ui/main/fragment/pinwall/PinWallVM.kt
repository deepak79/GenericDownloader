package com.mv.genericdownloader.ui.main.fragment.pinwall

import androidx.lifecycle.MutableLiveData
import com.mv.genericdownloader.data.DataManager
import com.mv.genericdownloader.model.response.DataResponse
import com.mv.genericdownloader.ui.base.BaseViewModel
import com.mv.genericdownloader.utils.rx.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class PinWallVM(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<PinWallNavigator>(dataManager, schedulerProvider) {
    //Livedata to notify response to fragment with viewmodel
    private var mDataResponse: MutableLiveData<MutableList<DataResponse>> =
        MutableLiveData()
    //List of data
    private var mData = mutableListOf<DataResponse>()
    //To maintain currentIndex
    private var mCurrentIndex = 0
    //No. of items per request
    private val OFFSET = 6
    private var mLastIndex = OFFSET

    /**
     * To show the error to the user
     * */
    private fun handleError(error: Throwable?) {
        setIsLoading(false)
        getNavigator().onHandleError(error?.message!!)
    }

    /**
     * To get the livedata
     * */
    fun getDataResponseLiveData(): MutableLiveData<MutableList<DataResponse>> {
        return mDataResponse
    }
    /**
     * To reset request
     * */
    fun reset() {
        mData = mutableListOf()
        mCurrentIndex = 0
        mLastIndex = OFFSET
    }

    /**
     * To request data from network
     * */
    fun getDataFromRemote() {
        setIsLoading(true)
        if (mData.size == 0) {
            compositeDisposable.add(
                dataManager.getData()
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ data ->
                        setIsLoading(false)
                        mData = data
                        mDataResponse.postValue(mData.subList(mCurrentIndex, mLastIndex))
                    }, { throwable ->
                        handleError(throwable)
                        mDataResponse.postValue(null)
                    })
            )
        } else {
            setIsLoading(false)
            //Mock pagination
            mCurrentIndex = mLastIndex
            mLastIndex += OFFSET
            compositeDisposable.add(
                Completable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                    .subscribe {
                        if (mCurrentIndex < mData.size && mLastIndex < mData.size) {
                            mDataResponse.postValue(mData.subList(mCurrentIndex, mLastIndex))
                        } else {
                            mDataResponse.postValue(mData.subList(mData.size - 7, mData.size - 1))
                        }
                    }
            )
        }
    }
}
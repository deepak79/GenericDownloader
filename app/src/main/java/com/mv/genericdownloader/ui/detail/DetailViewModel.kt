package com.mv.genericdownloader.ui.detail

import com.mv.genericdownloader.data.DataManager
import com.mv.genericdownloader.ui.base.BaseViewModel
import com.mv.genericdownloader.utils.rx.SchedulerProvider


class DetailViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<DetailNavigator>(dataManager, schedulerProvider) {

    private fun handleError(error: Throwable?) {
        setIsLoading(false)
        getNavigator().onHandleError(error?.message!!)
    }
}

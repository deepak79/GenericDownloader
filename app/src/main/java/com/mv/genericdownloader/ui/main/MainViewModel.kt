package com.mv.genericdownloader.ui.main

import com.mv.genericdownloader.data.DataManager
import com.mv.genericdownloader.ui.base.BaseViewModel
import com.mv.genericdownloader.utils.rx.SchedulerProvider


class MainViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider) :
    BaseViewModel<MainNavigator>(dataManager, schedulerProvider) {

    private fun handleError(error: Throwable?) {
        setIsLoading(false)
        getNavigator().onHandleError(error?.message!!)
    }
}

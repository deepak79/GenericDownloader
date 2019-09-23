package com.mv.genericdownloader.ui.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import com.mv.genericdownloader.data.DataManager
import com.mv.genericdownloader.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

abstract class BaseViewModel<N>(
    val dataManager: DataManager,
    val schedulerProvider: SchedulerProvider
) : ViewModel() {

    val isLoading = ObservableBoolean()

    val compositeDisposable: CompositeDisposable

    private var mNavigator: WeakReference<N>? = null

    fun getNavigator(): N {
        return mNavigator!!.get()!!
    }

    fun setNavigator(navigator: N) {
        this.mNavigator = WeakReference(navigator)
    }

    init {
        this.compositeDisposable = CompositeDisposable()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.set(isLoading)
    }
}

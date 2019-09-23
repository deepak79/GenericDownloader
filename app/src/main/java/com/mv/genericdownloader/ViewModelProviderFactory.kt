package com.mv.genericdownloader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mv.genericdownloader.data.DataManager
import com.mv.genericdownloader.ui.main.MainViewModel
import com.mv.genericdownloader.ui.main.fragment.pinwall.PinWallVM
import com.mv.genericdownloader.utils.rx.SchedulerProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelProviderFactory @Inject
constructor(
    val dataManager: DataManager,
    val schedulerProvider: SchedulerProvider
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(
                dataManager,
                schedulerProvider
            ) as T
            modelClass.isAssignableFrom(PinWallVM::class.java) -> PinWallVM(
                dataManager,
                schedulerProvider
            ) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
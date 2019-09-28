package com.mv.genericdownloader

import android.app.Activity
import android.app.Application
import com.androidnetworking.AndroidNetworking
import com.mv.genericdownloader.di.component.DaggerAppComponent
import com.mv.genericdownloader.utils.AppConstants.Companion.CACHE_SIZE
import com.mv.genericdownloaderlib.core.GenericDownloadManager
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): DispatchingAndroidInjector<Activity>? {
        return activityDispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()
        //Initialize networking library
        AndroidNetworking.initialize(applicationContext)

        //Initialize Generic Download Manager
        GenericDownloadManager.initialize(CACHE_SIZE)

        //Depedency injection
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }
}

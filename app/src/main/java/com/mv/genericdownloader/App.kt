package com.mv.genericdownloader

import android.app.Activity
import android.app.Application
import com.mv.genericdownloader.di.component.DaggerAppComponent
import com.androidnetworking.AndroidNetworking
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
        AndroidNetworking.initialize(applicationContext)
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }
}

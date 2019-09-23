package com.mv.genericdownloader.di.module

import android.app.Application
import android.content.Context
import com.mv.genericdownloader.data.AppDataManager
import com.mv.genericdownloader.data.DataManager
import com.mv.genericdownloader.data.remote.ApiHelper
import com.mv.genericdownloader.data.remote.AppApiHelper
import com.mv.genericdownloader.utils.rx.AppSchedulerProvider
import com.mv.genericdownloader.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule {

    @Provides
    @Singleton
    fun provideApiHelper(appApiHelper: AppApiHelper): ApiHelper {
        return appApiHelper
    }


    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideDataManager(appDataManager: AppDataManager): DataManager {
        return appDataManager
    }

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }
}
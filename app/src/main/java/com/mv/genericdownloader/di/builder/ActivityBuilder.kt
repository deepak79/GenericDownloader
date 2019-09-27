package com.mv.genericdownloader.di.builder

import com.mv.genericdownloader.ui.detail.DetailActivity
import com.mv.genericdownloader.ui.main.MainActivity
import com.mv.genericdownloader.ui.main.fragment.pinwall.PinWallFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(
        modules = [
            PinWallFragmentProvider::class]
    )
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindDetailActivity(): DetailActivity
}

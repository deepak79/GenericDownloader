package com.mv.genericdownloader.ui.main.fragment.pinwall

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PinWallFragmentProvider {
    @ContributesAndroidInjector
    abstract fun providePinWallFragment(): PinWallFragment
}
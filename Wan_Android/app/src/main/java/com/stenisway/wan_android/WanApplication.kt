package com.stenisway.wan_android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WanApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}
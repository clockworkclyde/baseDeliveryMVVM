package com.github.clockworkclyde.basedeliverymvvm

import android.app.Application
import com.chibatching.kotpref.Kotpref
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseDeliveryApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Kotpref.init(this)
    }
}
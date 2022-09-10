package com.github.clockworkclyde.basedeliverymvvm

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseDeliveryApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        FirebaseApp.initializeApp(this)
        Kotpref.init(this)
    }
}
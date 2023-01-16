package com.github.clockworkclyde.basedeliverymvvm.util

import timber.log.Timber

inline fun logg(crossinline onText: () -> String) {
   Timber.e(onText())
}

inline fun loggError(crossinline onThrowable: () -> Throwable) {
   Timber.e(onThrowable())
}
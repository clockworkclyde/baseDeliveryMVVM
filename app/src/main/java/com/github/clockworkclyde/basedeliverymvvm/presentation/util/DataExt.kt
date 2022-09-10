package com.github.clockworkclyde.basedeliverymvvm.presentation.util

fun String.matchesNumbersOnly() = matches("[a-zA-Z]+".toRegex()).not()
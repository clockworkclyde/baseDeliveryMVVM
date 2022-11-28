package com.github.clockworkclyde.basedeliverymvvm.util

fun String.matchesNumbersOnly() = matches("[a-zA-Z]+".toRegex()).not()

fun String.getAfterSeparator() = substring(indexOf("@") + 1, length)

fun String.getBeforeSeparator() = substring(0, indexOf("@"))
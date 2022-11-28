package com.github.clockworkclyde.models.local.auth

import com.chibatching.kotpref.KotprefModel

object TimeCounterPref : KotprefModel() {
    var millis by longPref()
}
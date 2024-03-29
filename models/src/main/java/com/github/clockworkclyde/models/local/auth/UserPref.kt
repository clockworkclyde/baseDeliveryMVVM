package com.github.clockworkclyde.models.local.auth

import com.chibatching.kotpref.KotprefModel

object UserPref : KotprefModel() {
    var uid by stringPref()
    var username by stringPref()
    var phone by stringPref()
}

data class User(val uid: String, val name: String, val phone: String)
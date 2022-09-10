package com.github.clockworkclyde.models.local.user

import com.chibatching.kotpref.KotprefModel

object UserPref : KotprefModel() {
    var uid by stringPref()
    var username by stringPref()
}

data class User(val uid: String, val name: String)
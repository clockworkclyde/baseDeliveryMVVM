package com.github.clockworkclyde.models.local.cart

import com.chibatching.kotpref.KotprefModel

object OrderCartPref : KotprefModel() {
    val dishes by stringSetPref()
}
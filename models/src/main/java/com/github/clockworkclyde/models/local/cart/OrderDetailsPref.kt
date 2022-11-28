package com.github.clockworkclyde.models.local.cart

import com.chibatching.kotpref.KotprefModel

object OrderDetailsPref : KotprefModel() {
    var addressId by stringPref()
}
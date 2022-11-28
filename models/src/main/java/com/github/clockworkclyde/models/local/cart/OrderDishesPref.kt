package com.github.clockworkclyde.models.local.cart

import com.chibatching.kotpref.KotprefModel

object OrderDishesPref : KotprefModel() {
    val orderDishesEntities by stringSetPref()
}



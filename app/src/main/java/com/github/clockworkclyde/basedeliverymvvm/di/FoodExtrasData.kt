package com.github.clockworkclyde.basedeliverymvvm.di

import com.github.clockworkclyde.basedeliverymvvm.R

sealed class FoodExtrasData(
    val id: Int, val title: String, val price: Int
) {
    object Cheese : FoodExtrasData(R.string.cheese_extra, CHEESE, CHEESE_PRICE)

    companion object {
        const val CHEESE = "Cheese"
        const val CHEESE_PRICE = 40
    }
}
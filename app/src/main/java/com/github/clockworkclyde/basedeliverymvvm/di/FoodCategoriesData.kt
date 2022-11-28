package com.github.clockworkclyde.basedeliverymvvm.di

import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.di.FoodCategories.BURGER
import com.github.clockworkclyde.basedeliverymvvm.di.FoodCategories.PIZZA
import com.github.clockworkclyde.basedeliverymvvm.di.FoodCategories.SUSHI

sealed class FoodCategoriesData(
    val category: String,
    val titleResId: Int,
    val extras: List<FoodExtrasData> = listOf()
) {
    object Pizza : FoodCategoriesData(PIZZA, R.string.pizza, DEFAULT_EXTRAS)
    object Sushi : FoodCategoriesData(SUSHI, R.string.sushi, DEFAULT_EXTRAS)
    object Burger : FoodCategoriesData(BURGER, R.string.burger, DEFAULT_EXTRAS)

    companion object {
        private val DEFAULT_EXTRAS = listOf(FoodExtrasData.Cheese)
    }
}
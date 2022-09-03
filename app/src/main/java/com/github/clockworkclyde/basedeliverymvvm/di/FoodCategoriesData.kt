package com.github.clockworkclyde.basedeliverymvvm.di

import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.di.FoodCategories.BURGER
import com.github.clockworkclyde.basedeliverymvvm.di.FoodCategories.PIZZA
import com.github.clockworkclyde.basedeliverymvvm.di.FoodCategories.SUSHI

sealed class FoodCategoriesData(
    val category: String,
    val titleResId: Int
) {
    object Pizza : FoodCategoriesData(PIZZA, R.string.pizza)
    object Sushi : FoodCategoriesData(SUSHI, R.string.sushi)
    object Burger : FoodCategoriesData(BURGER, R.string.burger)
}
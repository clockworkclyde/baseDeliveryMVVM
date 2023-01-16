package com.github.clockworkclyde.basedeliverymvvm.di

import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.di.FoodCategories.BURGER
import com.github.clockworkclyde.basedeliverymvvm.di.FoodCategories.PIZZA
import com.github.clockworkclyde.basedeliverymvvm.di.FoodCategories.SUSHI
import com.github.clockworkclyde.basedeliverymvvm.di.FoodCategories.TEA
import com.github.clockworkclyde.models.ui.dishes.extra.*

sealed class FoodCategoriesData(
   val category: String,
   val titleResId: Int,
) {
   object Pizza : FoodCategoriesData(PIZZA, R.string.pizza)
   object Sushi : FoodCategoriesData(SUSHI, R.string.sushi)
   object Burger : FoodCategoriesData(BURGER, R.string.burger)
   object Tea : FoodCategoriesData(TEA, R.string.tea)

   companion object {
      fun getExtrasCategory(category: FoodCategoriesData): List<ExtraCategory> {
         return when (category) {
            Pizza -> listOf(
               ExtraCategory(
                  title = "Choose pizza dough",
                  extras = mapExtras(PizzaCatExtra)
               )
            )
            Tea -> listOf(
               ExtraCategory(
                  title = "Choose drink amount",
                  extras = mapExtras(DrinkCatExtra),
                  quantityAccepted = false
               )
            )
            else -> listOf(
               ExtraCategory(
                  title = "Choose extras to add in (optional)",
                  extras = mapExtras(FoodCatExtra)
               )
            )
         }
      }

      private fun mapExtras(cat: IExtrasCategory) = cat.extras().map {
         DishExtra(
            id = it.id,
            title = it.title,
            price = it.price
         )
      }
   }
}


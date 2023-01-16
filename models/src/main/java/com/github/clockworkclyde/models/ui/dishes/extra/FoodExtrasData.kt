package com.github.clockworkclyde.models.ui.dishes.extra

import com.github.clockworkclyde.models.R
import com.github.clockworkclyde.models.lengthToPrice
import com.github.clockworkclyde.models.ui.dishes.extra.ExtrasConstants.Drinks.LARGE
import com.github.clockworkclyde.models.ui.dishes.extra.ExtrasConstants.Drinks.LARGE_PRICE
import com.github.clockworkclyde.models.ui.dishes.extra.ExtrasConstants.Drinks.MEDIUM
import com.github.clockworkclyde.models.ui.dishes.extra.ExtrasConstants.Drinks.MEDIUM_PRICE
import com.github.clockworkclyde.models.ui.dishes.extra.ExtrasConstants.Drinks.SMALL
import com.github.clockworkclyde.models.ui.dishes.extra.ExtrasConstants.Drinks.SMALL_PRICE
import com.github.clockworkclyde.models.ui.dishes.extra.ExtrasConstants.Foods.CHEESE
import com.github.clockworkclyde.models.ui.dishes.extra.ExtrasConstants.Foods.JALAPENO
import com.github.clockworkclyde.models.ui.dishes.extra.ExtrasConstants.Pizzas.CLASSIC
import com.github.clockworkclyde.models.ui.dishes.extra.ExtrasConstants.Pizzas.THIN_CRUST

object ExtrasConstants {

   object Foods {
      const val CHEESE = "Cheese"
      const val JALAPENO = "Jalapeno"
   }

   object Drinks {
      const val SMALL = "Small 0.3L"
      const val MEDIUM = "Medium 0.5L"
      const val LARGE = "Large 0.7L"
      const val SMALL_PRICE = 0
      const val MEDIUM_PRICE = 30
      const val LARGE_PRICE = 40
   }

   object Pizzas {
      const val CLASSIC = "Classic"
      const val THIN_CRUST = "Thin Crust"
   }
}

interface IExtra {
   val id: Int
   val title: String
   val price: Int
}

sealed interface IExtrasCategory {
   fun extras(): List<IExtra>
}

object FoodCatExtra : IExtrasCategory {

   override fun extras(): List<IExtra> = listOf(
      cheese, jalapeno
   )

   private val cheese = object : IExtra {
      override val id: Int = R.string.cheese_extra
      override val title: String = CHEESE
      override val price: Int = title.lengthToPrice()
   }
   private val jalapeno = object : IExtra {
      override val id: Int = R.string.jalapeno_extra
      override val title: String = JALAPENO
      override val price: Int = title.lengthToPrice()
   }


}

object DrinkCatExtra : IExtrasCategory {

   override fun extras(): List<IExtra> = listOf(
      small, medium, large
   )

   private val small = object : IExtra {
      override val id: Int = R.string.drink_small
      override val title: String = SMALL
      override val price: Int = SMALL_PRICE
   }
   private val medium = object : IExtra {
      override val id: Int = R.string.drink_medium
      override val title: String = MEDIUM
      override val price: Int = MEDIUM_PRICE
   }
   private val large = object : IExtra {
      override val id: Int = R.string.drink_large
      override val title: String = LARGE
      override val price: Int = LARGE_PRICE
   }
}

object PizzaCatExtra : IExtrasCategory {

   override fun extras(): List<IExtra> = listOf(
      classic, thin
   )

   private val classic = object : IExtra {
      override val id: Int = R.string.classic_pizza
      override val title: String = CLASSIC
      override val price: Int = title.lengthToPrice()
   }
   private val thin = object : IExtra {
      override val id: Int = R.string.thin_pizza
      override val title: String = THIN_CRUST
      override val price: Int = title.lengthToPrice()
   }
}
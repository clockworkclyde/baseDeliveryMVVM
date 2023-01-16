package com.github.clockworkclyde.basedeliverymvvm.providers.database.db

import androidx.room.TypeConverter
import com.github.clockworkclyde.models.ui.dishes.extra.ExtraCategory
import com.google.gson.Gson

class DishExtrasConverter {

   private val gson = Gson()

   @TypeConverter
   fun fromString(str: String): List<ExtraCategory> {
      return gson.fromJson(str, Array<ExtraCategory>::class.java).toList()
   }

   @TypeConverter
   fun fromList(list: List<ExtraCategory>): String {
      return gson.toJson(list)
   }

}
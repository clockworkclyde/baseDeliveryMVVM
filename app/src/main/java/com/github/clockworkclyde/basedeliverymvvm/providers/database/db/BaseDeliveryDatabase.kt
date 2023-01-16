package com.github.clockworkclyde.basedeliverymvvm.providers.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.clockworkclyde.basedeliverymvvm.providers.database.dao.DishesDao
import com.github.clockworkclyde.models.local.dishes.DishEntity

@Database(
   entities = [DishEntity::class],
   version = 2,
   exportSchema = false
)
@TypeConverters(DishExtrasConverter::class)
abstract class BaseDeliveryDatabase : RoomDatabase() {
    abstract fun deliveryDao(): DishesDao
}
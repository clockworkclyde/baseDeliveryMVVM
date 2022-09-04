package com.github.clockworkclyde.basedeliverymvvm.providers.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.clockworkclyde.basedeliverymvvm.providers.database.dao.DishesDao
import com.github.clockworkclyde.models.local.main.DishEntity

@Database(
    entities = [DishEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BaseDeliveryDatabase : RoomDatabase() {
    abstract fun deliveryDao(): DishesDao
    //abstract fun orderCartDao(): OrderCartDao
}
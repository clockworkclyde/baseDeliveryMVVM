package com.github.clockworkclyde.basedeliverymvvm.providers.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.clockworkclyde.basedeliverymvvm.providers.database.DishesDao
import com.github.clockworkclyde.basedeliverymvvm.providers.database.OrderCartDao
import com.github.clockworkclyde.models.local.cart.OrderCartEntity
import com.github.clockworkclyde.models.local.main.DishEntity

@Database(
    entities = [DishEntity::class, OrderCartEntity::class],
    version = 4,
    exportSchema = false
)
abstract class BaseDeliveryDatabase : RoomDatabase() {
    abstract fun deliveryDao(): DishesDao
    abstract fun orderCartDao(): OrderCartDao
}
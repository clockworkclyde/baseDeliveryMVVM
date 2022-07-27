package com.github.clockworkclyde.basedeliverymvvm.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.clockworkclyde.basedeliverymvvm.database.dao.MenuCategoriesDao
import com.github.clockworkclyde.basedeliverymvvm.database.dao.OrderCartDao
import com.github.clockworkclyde.basedeliverymvvm.database.entities.cart.OrderCartItem
import com.github.clockworkclyde.basedeliverymvvm.database.entities.main.CachedCategory
import com.github.clockworkclyde.basedeliverymvvm.database.entities.main.CachedCategoryItem

@Database(
    entities = [CachedCategory::class, CachedCategoryItem::class, OrderCartItem::class],
    version = 2,
    exportSchema = false
)
abstract class BaseDeliveryDatabase : RoomDatabase() {
    abstract fun deliveryDao(): MenuCategoriesDao
    abstract fun orderCartDao(): OrderCartDao
}
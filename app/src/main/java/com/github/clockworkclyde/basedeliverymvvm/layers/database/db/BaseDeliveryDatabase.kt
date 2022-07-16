package com.github.clockworkclyde.basedeliverymvvm.layers.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.clockworkclyde.basedeliverymvvm.layers.database.dao.MenuCategoriesDao
import com.github.clockworkclyde.basedeliverymvvm.layers.database.dao.OrderCartDao
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.cart.OrderCartItem
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main.Category
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main.CategoryItem

@Database(
    entities = [Category::class, CategoryItem::class, OrderCartItem::class],
    version = 1,
    exportSchema = false
)
abstract class BaseDeliveryDatabase : RoomDatabase() {
    abstract fun deliveryDao(): MenuCategoriesDao
    abstract fun orderCartDao(): OrderCartDao
}
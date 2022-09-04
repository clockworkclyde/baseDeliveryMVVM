package com.github.clockworkclyde.basedeliverymvvm.data.datasource

import com.github.clockworkclyde.basedeliverymvvm.providers.database.dao.DishesDao
import com.github.clockworkclyde.models.local.main.DishEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeliveryLocalDataSourceImpl @Inject constructor(private val categoriesDao: DishesDao) {

    fun getDishesByCategoryId(id: Int): Flow<List<DishEntity>> {
        return categoriesDao.getDishesByCategoryId(id)
    }

    fun searchDishes(query: String): Flow<List<DishEntity>> {
        return categoriesDao.searchDishesByQuery(query)
    }

    suspend fun insertDishes(categoriesWithItems: List<DishEntity>) {
        categoriesDao.insertDishes(categoriesWithItems)
    }

    suspend fun clearDishes() {
        categoriesDao.clearAllDishes()
    }

}
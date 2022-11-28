package com.github.clockworkclyde.basedeliverymvvm.data.datasource

import com.github.clockworkclyde.basedeliverymvvm.providers.database.dao.DishesDao
import com.github.clockworkclyde.models.local.dishes.DishEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DishesLocalDataSourceImpl @Inject constructor(private val dishesDao: DishesDao) {

    fun getDishesByCategoryId(id: Int): Flow<List<DishEntity>> {
        return dishesDao.getDishesByCategoryId(id)
    }

    suspend fun getAllDishes(): List<DishEntity> {
        return dishesDao.getAllDishes()
    }

    fun searchDishes(query: String): Flow<List<DishEntity>> {
        return dishesDao.searchDishesByQuery(query)
    }

    suspend fun searchDishById(id: Long): DishEntity? {
        return dishesDao.searchDishById(id)
    }

    suspend fun insertDishes(categoriesWithItems: List<DishEntity>) {
        dishesDao.insertDishes(categoriesWithItems)
    }

    suspend fun clearDishes() {
        dishesDao.clearAllDishes()
    }

}
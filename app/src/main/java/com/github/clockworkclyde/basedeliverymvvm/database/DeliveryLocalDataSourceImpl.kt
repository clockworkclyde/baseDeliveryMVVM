package com.github.clockworkclyde.basedeliverymvvm.database

import com.github.clockworkclyde.basedeliverymvvm.database.dao.MenuCategoriesDao
import com.github.clockworkclyde.basedeliverymvvm.database.entities.main.CachedCategoryItem
import com.github.clockworkclyde.basedeliverymvvm.database.entities.main.CachedCategoryWithItems
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeliveryLocalDataSourceImpl @Inject constructor(private val categoriesDao: MenuCategoriesDao) {

    fun getLatestCategoriesData(): List<CachedCategoryWithItems> {
        return categoriesDao.dataCategoriesWithItems()
    }

    fun searchInCategoriesData(query: String): Flow<List<CachedCategoryItem>> {
        return categoriesDao.searchCategoriesItems(query)
    }

    suspend fun insertCategoriesWithItems(categoriesWithItems: List<CachedCategoryWithItems>) {
        categoriesDao.insertCategoriesWithItems(categoriesWithItems)
    }

    suspend fun clearCategoriesAndItems() {
        categoriesDao.clearCategoriesList()
        categoriesDao.clearCategoriesItemsList()
    }

}
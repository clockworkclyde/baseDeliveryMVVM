package com.github.clockworkclyde.basedeliverymvvm.layers.database

import com.github.clockworkclyde.basedeliverymvvm.layers.database.dao.MenuCategoriesDao
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main.CachedCategory
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main.CachedCategoryItem
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main.CachedCategoryWithItems
import com.github.clockworkclyde.network.api.model.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
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
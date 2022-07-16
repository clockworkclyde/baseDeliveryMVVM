package com.github.clockworkclyde.basedeliverymvvm.layers.database

import com.github.clockworkclyde.basedeliverymvvm.layers.database.dao.MenuCategoriesDao
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main.Category
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main.CategoryItem
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main.CategoryWithItems
import com.github.clockworkclyde.network.api.model.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.UnknownHostException
import javax.inject.Inject

class DeliveryLocalDataSourceImpl @Inject constructor(private val categoriesDao: MenuCategoriesDao) {

    private val _dataCategoriesStateFlow =
        MutableStateFlow<PagingState<Flow<List<Category>>>>(PagingState.Loading)
    val dataCategoriesStateFlow = _dataCategoriesStateFlow.asStateFlow()

    //todo remove similar stateflow ?
    private val _dataCategoriesItemsStateFlow =
        MutableStateFlow<PagingState<Flow<List<CategoryItem>>>>(PagingState.Loading)
    val dataCategoriesItemsStateFlow = _dataCategoriesItemsStateFlow.asStateFlow()

    private val _dataCategoriesWithItemsStateFlow =
        MutableStateFlow<PagingState<Flow<List<CategoryWithItems>>>>(PagingState.Loading)
    val dataCategoriesWithItemsStateFlow = _dataCategoriesWithItemsStateFlow.asStateFlow()


    /** get all categories and its menu items**/
    suspend fun getLatestCategoriesData() {
        if (_dataCategoriesWithItemsStateFlow.value is PagingState.Loading) {
            try {
                withContext(Dispatchers.IO) {
                    val response = categoriesDao.dataCategoriesWithItems()
                    _dataCategoriesWithItemsStateFlow.emit(PagingState.Content(response))
                }
            } catch (e: Exception) {
                val errorState = PagingState.Error(e)
                _dataCategoriesWithItemsStateFlow.emit(errorState)
            }
        }
    }

    /**db operations for all categories**/
    suspend fun insertLatestCategories(categories: List<Category>) {
        if (_dataCategoriesStateFlow.value !is PagingState.Error) {
            withContext(Dispatchers.IO) {
                categoriesDao.insertCategories(categories)
            }
        }
    }

    //todo remove suspend or withContext,test
    suspend fun getLatestCategories() {
        if (_dataCategoriesStateFlow.value is PagingState.Loading) {
            try {
                withContext(Dispatchers.IO) {
                    val response = categoriesDao.dataCategories()
                    _dataCategoriesStateFlow.emit(PagingState.Content(response))
                }
            } catch (e: UnknownHostException) {
                throw UnknownHostException("It's an error when request categories in pagingState $_dataCategoriesStateFlow")
            }
        }
    }

    suspend fun clearCategories() = categoriesDao.clearCategoriesList()

    /** db operations for all category items **/
    suspend fun insertLatestCategoriesItems(items: List<CategoryItem>) {
        if (_dataCategoriesStateFlow.value !is PagingState.Error) {
            withContext(Dispatchers.IO) {
                categoriesDao.insertCategoriesItems(items)
            }
        }
    }

    //todo with <T>fun
    suspend fun getLatestCategoriesItems() {
        if (_dataCategoriesItemsStateFlow.value is PagingState.Loading) {
            try {
                withContext(Dispatchers.IO) {
                    if (_dataCategoriesItemsStateFlow.value is PagingState.Loading) {
                        val response = categoriesDao.dataTotalCategoriesItems()
                        _dataCategoriesItemsStateFlow.emit(PagingState.Content(response))
                    }
                }
            } catch (e: UnknownHostException) {
                throw UnknownHostException("It's an error when request categories items in pagingState $_dataCategoriesStateFlow")
            }
        }
    }

    suspend fun clearCategoriesItems() = categoriesDao.clearCategoriesItemsList()

}
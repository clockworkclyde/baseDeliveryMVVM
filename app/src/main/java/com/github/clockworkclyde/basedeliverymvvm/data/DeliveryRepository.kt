package com.github.clockworkclyde.basedeliverymvvm.data

import android.content.Context
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuCategoryUiModel
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuItemProgressModel
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.model.menu.MenuItemUiModel
import com.github.clockworkclyde.basedeliverymvvm.database.DeliveryLocalDataSourceImpl
import com.github.clockworkclyde.basedeliverymvvm.database.entities.main.CachedCategory
import com.github.clockworkclyde.basedeliverymvvm.database.entities.main.CachedCategoryItem
import com.github.clockworkclyde.basedeliverymvvm.database.entities.main.CachedCategoryWithItems
import com.github.clockworkclyde.network.api.DeliveryRemoteDataSourceImpl
import com.github.clockworkclyde.network.api.model.MenuItemDto
import com.github.clockworkclyde.network.api.model.PagingState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.net.UnknownHostException
import javax.inject.Inject

class DeliveryRepository @Inject constructor(
    private val remoteDataSource: DeliveryRemoteDataSourceImpl,
    private val localDataSource: DeliveryLocalDataSourceImpl,
    private val context: Context
) {

    private val dataState =
        MutableStateFlow<PagingState<List<MenuCategoryUiModel>>>(PagingState.Loading)

    fun data(): Flow<List<MenuCategoryUiModel>> = dataState.map { state ->
        when (state) {
            is PagingState.Loading -> {
                val progressObjects = IntRange(1, 10).map { MenuItemProgressModel }
                listOf(MenuCategoryUiModel("_", progressObjects))
            }
            is PagingState.Content -> state.data
            is PagingState.Error -> throw UnknownHostException("Network mechanism error") //todo handler
            else -> throw NullPointerException("wrong state exception for paging state $state")
        }
    }

    // to do: move some logic to room cache class
    /** request available data from database **/
    suspend fun init(isForcedRefresh: Boolean = false) {
        coroutineScope {
            if (isForcedRefresh || dataState.value is PagingState.Error) {
                localDataSource.clearCategoriesAndItems()
                val categories = listOf(
                    context.getString(R.string.kfc),
                    context.getString(R.string.pizza),
                    context.getString(
                        R.string.sushi
                    )
                )
                val runningTasks = categories.map { category ->
                    async {
                        val items = remoteDataSource.initLoading(category)
                        category to items
                    }
                }
                val categoryModels = runningTasks.awaitAll().map {
                    val cachedCategory = CachedCategory(it.first, it.first.hashCode().toLong())
                    val cachedCategoryItems =
                        mapDtoToEntity(it.second, it.first.hashCode().toLong())
                    CachedCategoryWithItems(cachedCategory, cachedCategoryItems)
                }
                localDataSource.insertCategoriesWithItems(categoryModels)
            }
            val mappedDbResponse =
                withContext(Dispatchers.IO) {
                    localDataSource.getLatestCategoriesData().map {
                        MenuCategoryUiModel(it.category.title, mapEntityToUi(it.items))
                    }
                }
            if (mappedDbResponse.isEmpty()) {
                init(true)
            }
            dataState.emit(PagingState.Content(mappedDbResponse))
        }
    }

    fun search(query: String): Flow<List<MenuItemUiModel>> {
        val items = localDataSource.searchInCategoriesData(query).map { dbItems ->
            dbItems.map {
                MenuItemUiModel(
                    id = it.id,
                    title = it.title,
                    image = it.imageUrl,
                    price = it.price,
                    servingSize = it.servingSize
                )
            }
        }
        return items
    }


    private fun mapDtoToEntity(
        items: List<MenuItemDto>,
        categoryId: Long
    ): List<CachedCategoryItem> =
        items.map {
            CachedCategoryItem(
                id = it.id,
                title = it.title,
                imageUrl = it.image,
                price = 399,
                servingSize = it.servingSize ?: "",
                categoryId = categoryId
            )
        }

    private fun mapEntityToUi(items: List<CachedCategoryItem>): List<MenuItemUiModel> = items.map {
        MenuItemUiModel(
            id = it.id,
            title = it.title,
            image = it.imageUrl,
            price = it.price,
            servingSize = it.servingSize
        )
    }
}
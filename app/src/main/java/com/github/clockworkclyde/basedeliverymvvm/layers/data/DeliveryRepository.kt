package com.github.clockworkclyde.basedeliverymvvm.layers.data

import android.content.Context
import com.github.clockworkclyde.basedeliverymvvm.R
import com.github.clockworkclyde.basedeliverymvvm.layers.data.model.MenuCategoryUiModel
import com.github.clockworkclyde.basedeliverymvvm.layers.data.model.MenuItemProgressModel
import com.github.clockworkclyde.basedeliverymvvm.layers.data.model.MenuItemUiModel
import com.github.clockworkclyde.basedeliverymvvm.layers.database.DeliveryLocalDataSourceImpl
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main.Category
import com.github.clockworkclyde.network.api.DeliveryRemoteDataSourceImpl
import com.github.clockworkclyde.network.api.model.MenuItemDto
import com.github.clockworkclyde.network.api.model.PagingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.NullPointerException

class DeliveryRepository @Inject constructor(
    private val remoteDataSource: DeliveryRemoteDataSourceImpl,
    private val localDataSource: DeliveryLocalDataSourceImpl,
    private val context: Context
) {

    private val dataState = MutableStateFlow<List<MenuCategoryUiModel>>(emptyList())

    fun data(): Flow<List<MenuCategoryUiModel>> =
        remoteDataSource.state.map { state ->
            when (state) {
                is PagingState.Loading -> {
                    val progressObjects = IntRange(1, 10).map { MenuItemProgressModel }
                    listOf(MenuCategoryUiModel("_", progressObjects))
                }
                is PagingState.Content -> state.data.map { (category, dtoList) ->
                    val mappedList = mapDtoToUiModel(dtoList)
                    MenuCategoryUiModel(
                        category = category,
                        items = mappedList
                    )
                }
                is PagingState.Error -> throw UnknownHostException("Network mechanism error") //todo handler
                else -> throw NullPointerException("wrong state exception for paging state $state")
            }
        }

    suspend fun init() {
        val categories = listOf(
            context.getString(R.string.asian_food),
            context.getString(R.string.pizza),
            context.getString(
                R.string.idk_restaurant
            )
        )
        val task = remoteDataSource.initLoading(categories)
        localDataSource.clearCategories()
        val categoriesModel = categories.map {
            Category(
                title = it
            )
        }
        localDataSource.insertLatestCategories(categoriesModel)

    }

    /** request available data from database **/
    suspend fun refresh(isForced: Boolean = false) {
        if (isForced || remoteDataSource.state.value is PagingState.Error || dataState.value.isEmpty()) init()
        else {
            localDataSource.dataCategoriesWithItemsStateFlow.map {

            }
        }
    }

    private fun mapDtoToUiModel(objects: List<MenuItemDto>): List<MenuItemUiModel> = objects.map {
        MenuItemUiModel(
            id = it.id,
            title = it.title,
            image = it.image,
            servingSize = it.servingSize ?: "",
            price = 499.99
        )
    }
}
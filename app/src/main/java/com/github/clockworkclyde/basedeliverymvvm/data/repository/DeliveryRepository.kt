package com.github.clockworkclyde.basedeliverymvvm.data.repository

import com.github.clockworkclyde.basedeliverymvvm.data.datasource.DeliveryLocalDataSourceImpl
import com.github.clockworkclyde.basedeliverymvvm.di.FoodCategoriesData
import com.github.clockworkclyde.models.local.main.DishEntity
import com.github.clockworkclyde.models.ui.menu.DishItem
import com.github.clockworkclyde.models.ui.menu.DishesCategoryItem
import com.github.clockworkclyde.network.api.DeliveryRemoteDataSourceImpl
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DeliveryRepository @Inject constructor(
    private val remoteDataSource: DeliveryRemoteDataSourceImpl,
    private val localDataSource: DeliveryLocalDataSourceImpl
) {

    fun getDishes(): Flow<List<DishesCategoryItem>> {
        val flowsList = getDishesByCategories(getCategories())
        return combine(flowsList) {
            it.map { dishes ->
                DishesCategoryItem(dishes.first().categoryId, dishes)
            }
        }
    }

    private fun getDishesByCategories(categories: List<FoodCategoriesData>): List<Flow<List<DishItem>>> {
        return categories.map { cat ->
            localDataSource.getDishesByCategoryId(cat.titleResId).map { entities ->
                entities.map { it.convertTo() }
            }
        }
    }

    /** fetch fresh data from remote api and save it in database **/
    suspend fun fetchDishes() {
        coroutineScope {
            localDataSource.clearDishes()
            val categories = getCategories()
            val list = mutableListOf<DishEntity>()
            categories.map { cat ->
                async {
                    remoteDataSource.initLoading(cat.category)
                        .map { it.convertTo(cat.titleResId) }
                        .let { list.addAll(it) }
                }
            }.awaitAll()
            insertFetchedDishes(list.toList())
        }
    }

    private suspend fun insertFetchedDishes(entities: List<DishEntity>) {
        localDataSource.insertDishes(entities)
    }

    /** find items by query or empty list using roomDatabase **/
    fun search(query: String): Flow<List<DishItem>> {
        val items = localDataSource.searchDishes(query).map { dbItems ->
            dbItems.map {
                DishItem(
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

    private fun getCategories(): List<FoodCategoriesData> {
        return listOf(FoodCategoriesData.Pizza, FoodCategoriesData.Sushi, FoodCategoriesData.Burger)
    }
}
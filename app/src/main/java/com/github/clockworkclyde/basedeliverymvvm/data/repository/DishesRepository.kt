package com.github.clockworkclyde.basedeliverymvvm.data.repository


import com.github.clockworkclyde.basedeliverymvvm.data.datasource.DishesLocalDataSourceImpl
import com.github.clockworkclyde.basedeliverymvvm.di.FoodCategoriesData
import com.github.clockworkclyde.basedeliverymvvm.di.FoodExtrasData
import com.github.clockworkclyde.models.local.dishes.DishEntity
import com.github.clockworkclyde.models.ui.dishes.DishItem
import com.github.clockworkclyde.models.ui.dishes.DishesCategoryItem
import com.github.clockworkclyde.models.ui.order.DishExtraEntity
import com.github.clockworkclyde.network.api.DeliveryRemoteDataSourceImpl
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject


class DishesRepository @Inject constructor(
    private val remoteDataSource: DeliveryRemoteDataSourceImpl,
    private val localDataSource: DishesLocalDataSourceImpl
) {

    fun getDishes(): Flow<List<DishesCategoryItem>> {
        val flowsList = getDishesByCategories()
        return combine(flowsList) {
            it.map { dishes ->
                DishesCategoryItem(dishes.firstOrNull()?.categoryId, dishes)
            }
        }
    }

    private fun getDishesByCategories(): List<Flow<List<DishItem>>> {
        return getCategories().map { category ->
            localDataSource.getDishesByCategoryId(category.titleResId)
                .map { it.map { entity -> entity.convertTo() } }
        }
    }

    suspend fun getAllDishes(): List<DishItem> {
        return localDataSource.getAllDishes()
            .map { it.convertTo() }
            .sortedBy { it.title.first() }
    }

    suspend fun loadDishesByCategories() {
        coroutineScope {
            clearLatestDishes()
            val list = mutableListOf<DishEntity>()
            getCategories().map { cat ->
                async {
                    remoteDataSource.initLoading(cat.category)
                        .mapTo(list) { it.convertTo(cat.titleResId) }
                }
            }.awaitAll()
            insertFetchedDishes(list.toList())
        }
    }

    private suspend fun clearLatestDishes() {
        localDataSource.clearDishes()
    }

    private suspend fun insertFetchedDishes(entities: List<DishEntity>) {
        localDataSource.insertDishes(entities)
    }

    fun search(query: String): Flow<List<DishItem>> {
        return localDataSource.searchDishes(query).map { entities ->
            entities.map { it.convertTo() }
        }
    }

    suspend fun search(id: Long): DishItem? {
        return localDataSource.searchDishById(id)?.convertTo()
    }

    fun getCategories(): List<FoodCategoriesData> {
        return listOf(FoodCategoriesData.Pizza, FoodCategoriesData.Sushi, FoodCategoriesData.Burger)
    }

    fun getExtrasForCategory(id: Int): List<FoodExtrasData> {
        return getCategories().find { it.titleResId == id }?.extras ?: emptyList()
    }

    fun searchExtras(category: Int, extras: Map<Int, Int>): List<DishExtraEntity> {
        return extras.mapNotNull { extra ->
            getExtrasForCategory(category).find { it.id == extra.key }?.let {
                DishExtraEntity(
                    id = it.id,
                    title = it.title,
                    quantity = extra.value,
                    price = it.price
                )
            }
        }
    }
}
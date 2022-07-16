package com.github.clockworkclyde.basedeliverymvvm.layers.database.dao

import androidx.room.*
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main.CategoryWithItems
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main.Category
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main.CategoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuCategoriesDao {

    @Transaction
    @Query("SELECT * FROM categories")
    fun dataCategoriesWithItems(): Flow<List<CategoryWithItems>>

    //todo data flow for ui
    @Query("SELECT * FROM categories")
    fun dataCategories(): Flow<List<Category>>

    //todo maybe source for search logic?
    @Query("SELECT * FROM categoriesItems")
    fun dataTotalCategoriesItems(): Flow<List<CategoryItem>>

//    @Query("SELECT * FROM main_category_menu_items") where like %
//    fun searchTotalCategoriesItems(query: String): Flow<List<CategoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<Category>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoriesItems(items: List<CategoryItem>)

    @Query("DELETE FROM categories")
    suspend fun clearCategoriesList()

    @Query("DELETE FROM categoriesItems")
    suspend fun clearCategoriesItemsList()
}
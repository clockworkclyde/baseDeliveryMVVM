package com.github.clockworkclyde.basedeliverymvvm.layers.database.dao

import androidx.room.*
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main.CachedCategoryWithItems
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main.CachedCategory
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main.CachedCategoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuCategoriesDao {

    @Transaction
    @Query("SELECT * FROM categories")
    fun dataCategoriesWithItems(): List<CachedCategoryWithItems>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoriesComponents(
        category: CachedCategory,
        items: List<CachedCategoryItem>
    )

    suspend fun insertCategoriesWithItems(categoriesWithItems: List<CachedCategoryWithItems>) {
        for (entry in categoriesWithItems) {
            insertCategoriesComponents(entry.category, entry.items)
        }
    }

    @Query("SELECT * FROM categoriesItems WHERE title LIKE '%' || :query || '%' ORDER BY title ASC")
    fun searchCategoriesItems(query: String): Flow<List<CachedCategoryItem>>

    @Query("DELETE FROM categories")
    suspend fun clearCategoriesList()

    @Query("DELETE FROM categoriesItems")
    suspend fun clearCategoriesItemsList()
}
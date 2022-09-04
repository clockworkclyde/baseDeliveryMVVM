package com.github.clockworkclyde.basedeliverymvvm.providers.database.dao

import androidx.room.*
import com.github.clockworkclyde.models.local.main.DishEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DishesDao {

    @Query("SELECT * FROM DishEntity WHERE id_category_owner = :id")
    fun getDishesByCategoryId(id: Int): Flow<List<DishEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDishes(items: List<DishEntity>)

    @Query("SELECT * FROM DishEntity WHERE title LIKE '%' || :query || '%' ORDER BY title ASC")
    fun searchDishesByQuery(query: String): Flow<List<DishEntity>>

    @Query("DELETE FROM DishEntity")
    suspend fun clearAllDishes()
}
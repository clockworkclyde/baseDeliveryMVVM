package com.github.clockworkclyde.basedeliverymvvm.providers.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.clockworkclyde.models.local.dishes.DishEntity
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
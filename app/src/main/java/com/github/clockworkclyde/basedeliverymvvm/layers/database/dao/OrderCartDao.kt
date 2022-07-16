package com.github.clockworkclyde.basedeliverymvvm.layers.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.cart.OrderCartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderCartDao {

    @Query("SELECT * FROM order_cart_items")
    fun dataOrderCartItems(): Flow<List<OrderCartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderCartItem(item: OrderCartItem)

    @Query("DELETE FROM order_cart_items WHERE external_id LIKE :itemId")
    suspend fun deleteOrderCartItem(itemId: Int)

}
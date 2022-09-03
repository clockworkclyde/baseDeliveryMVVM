package com.github.clockworkclyde.basedeliverymvvm.providers.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.clockworkclyde.models.local.cart.OrderCartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderCartDao {

    @Query("SELECT * FROM order_cart_items")
    fun dataOrderCartItems(): Flow<List<OrderCartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderCartItem(item: OrderCartEntity)

    @Query("DELETE FROM order_cart_items WHERE external_id LIKE :itemId")
    suspend fun deleteOrderCartItem(itemId: Long)

    @Query("UPDATE order_cart_items SET total_amount = :amount WHERE external_id = :itemId")
    suspend fun updateQuantityInOrder(itemId: Long, amount: Int)

}
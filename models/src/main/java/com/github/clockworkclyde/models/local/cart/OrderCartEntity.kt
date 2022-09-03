package com.github.clockworkclyde.models.local.cart

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_cart_items")
data class OrderCartEntity(
    @ColumnInfo(name = "external_id") @PrimaryKey(autoGenerate = false) val id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "serving_size") val servingSize: String,
    @ColumnInfo(name = "item_price") val price: Int,
    @ColumnInfo(name = "total_amount") val count: Int = 1
)
package com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categoriesItems")
data class CategoryItem(
    @ColumnInfo(name = "id_external") @PrimaryKey(autoGenerate = false) val id: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "price_rubles") val price: Double,
    @ColumnInfo(name = "serving_size_type") val servingSize: String,

    @ColumnInfo(name = "id_category_owner") val categoryId: Long = 0
)
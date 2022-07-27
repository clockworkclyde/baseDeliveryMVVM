package com.github.clockworkclyde.basedeliverymvvm.database.entities.main

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "categoriesItems",
    foreignKeys = [ForeignKey(
        entity = CachedCategory::class,
        parentColumns = ["id_internal"],
        childColumns = ["id_category_owner"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index("id_category_owner")]
)
data class CachedCategoryItem(
    @ColumnInfo(name = "id_external") @PrimaryKey(autoGenerate = false) val id: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "price_rubles") val price: Double,
    @ColumnInfo(name = "serving_size_type") val servingSize: String,

    @ColumnInfo(name = "id_category_owner") val categoryId: Long = 0
)
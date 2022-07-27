package com.github.clockworkclyde.basedeliverymvvm.database.entities.main

import androidx.room.*

@Entity(tableName = "categories")
data class CachedCategory(
    @ColumnInfo(name = "category_title") val title: String,
    @ColumnInfo(name = "id_internal") @PrimaryKey(autoGenerate = false) val id: Long
)

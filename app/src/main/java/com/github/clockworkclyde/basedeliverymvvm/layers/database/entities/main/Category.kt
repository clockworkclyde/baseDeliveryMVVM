package com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main

import androidx.room.*

@Entity(tableName = "categories")
data class Category(
    @ColumnInfo(name = "category_title") val title: String,
    @ColumnInfo(name = "id_internal") @PrimaryKey(autoGenerate = true) val id: Long = 0
)

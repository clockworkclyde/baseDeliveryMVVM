package com.github.clockworkclyde.basedeliverymvvm.database.entities.main

import androidx.room.Embedded
import androidx.room.Relation

data class CachedCategoryWithItems(
    @Embedded val category: CachedCategory,
    @Relation(
        parentColumn = "id_internal",
        entityColumn = "id_category_owner"
    ) val items: List<CachedCategoryItem>
)
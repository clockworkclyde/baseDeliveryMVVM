package com.github.clockworkclyde.basedeliverymvvm.layers.database.entities.main

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithItems(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id_internal",
        entityColumn = "id_category_owner"
    ) val items: List<CategoryItem>
)
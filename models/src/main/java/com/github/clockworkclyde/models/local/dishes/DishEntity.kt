package com.github.clockworkclyde.models.local.dishes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.clockworkclyde.models.mappers.IConvertableTo
import com.github.clockworkclyde.models.ui.dishes.DishItem
import com.github.clockworkclyde.models.ui.dishes.extra.ExtraCategory

@Entity
data class DishEntity(
   @ColumnInfo(name = "id_external") @PrimaryKey(autoGenerate = false) val id: Long = 0,
   @ColumnInfo(name = "title") val title: String,
   @ColumnInfo(name = "image_url") val imageUrl: String,
   @ColumnInfo(name = "price_rubles") val price: Int,
   @ColumnInfo(name = "serving_size_type") val servingSize: String,

   @ColumnInfo(name = "id_category_owner") val categoryId: Int = 0,
   @ColumnInfo(name = "extras") val extras: List<ExtraCategory> = emptyList()
) : IConvertableTo<DishItem> {
    override fun convertTo(): DishItem {
        return DishItem(
           id = id,
           title = title,
           image = imageUrl,
           price = price,
           servingSize = servingSize,
           categoryId = categoryId,
           extras = extras
        )
    }
}
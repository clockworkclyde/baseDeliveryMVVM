package com.github.clockworkclyde.models.ui.address

import com.github.clockworkclyde.models.ui.base.ListItem
import com.mapbox.geojson.Point

data class AddressItem(
    val id: String,
    val name: String,
    val coordinates: Point?,
    val place: String = "",
    val street: String,
    val house: String,
    val apartment: String = "",
    val entrance: String = "",
    val doorPhone: String = "",
    var isDefault: Boolean = false
) : ListItem {
    override val itemId: Long = id.hashCode().toLong()
    val addressId: String = (id + house + apartment).hashCode().toString()
}
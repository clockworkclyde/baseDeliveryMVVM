package com.github.clockworkclyde.models.local.cart

data class OrderDishesEntity(
    val quantity: Int,
    val id: Long,
    val additionTime: Long,
    val extrasIds: Map<Int, Int> = emptyMap()
)




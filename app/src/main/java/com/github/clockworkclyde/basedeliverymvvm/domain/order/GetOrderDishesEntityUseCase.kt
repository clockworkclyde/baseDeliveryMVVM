package com.github.clockworkclyde.basedeliverymvvm.domain.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.chibatching.kotpref.livedata.asLiveData
import com.github.clockworkclyde.models.local.cart.OrderDishesEntity
import com.github.clockworkclyde.models.local.cart.OrderDishesPref
import com.google.gson.Gson
import javax.inject.Inject

class GetOrderDishesEntityUseCase @Inject constructor(private val gson: Gson) {

    operator fun invoke(): LiveData<List<OrderDishesEntity>> {
        return OrderDishesPref.asLiveData(OrderDishesPref::orderDishesEntities).map { list ->
            list.map { gson.fromJson(it, OrderDishesEntity::class.java) }
        }
    }
}
package com.github.clockworkclyde.basedeliverymvvm.data.repository

import com.github.clockworkclyde.models.remote.base.Response
import com.github.clockworkclyde.models.ui.address.AddressItem
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserAddressRepository @Inject constructor(
    private val usersReference: DatabaseReference,
    private val gson: Gson
) {

    suspend fun getUserAddresses(uid: String): Response<List<AddressItem>> {
        try {
            usersReference.child(uid).child("addresses").get().await().children.map { snap ->
                snap.getValue(String::class.java)!!
            }.also { results ->
                return Response.Success(results.map { gson.fromJson(it, AddressItem::class.java) })
            }
        } catch (e: Exception) {
            return Response.Error(e)
        }
    }

    suspend fun getUserAddressById(uid: String, id: String): AddressItem? {
        try {
            usersReference.child(uid).child("addresses").get().await().child(id)
                .getValue(String::class.java).also { address ->
                return gson.fromJson(address, AddressItem::class.java)
            }
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun setUserAddress(uid: String, address: AddressItem) {
        withContext(Dispatchers.IO) {
            usersReference
                .child(uid)
                .child("addresses")
                .child(address.addressId)
                .setValue(gson.toJson(address)).await()
        }
    }
}
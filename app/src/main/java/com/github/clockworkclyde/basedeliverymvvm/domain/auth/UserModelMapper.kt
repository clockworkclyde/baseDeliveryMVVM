package com.github.clockworkclyde.basedeliverymvvm.domain.auth

import com.github.clockworkclyde.models.remote.auth.UserModel
import com.google.firebase.auth.FirebaseUser

class UserModelMapper {

    operator fun invoke(firebaseUser: FirebaseUser): UserModel {
        val uid = firebaseUser.uid
        val name = firebaseUser.displayName ?: "User@${firebaseUser.uid.hashCode()}"
        val phone = firebaseUser.phoneNumber ?: ""
        return UserModel(uid, name, phone)
    }
}
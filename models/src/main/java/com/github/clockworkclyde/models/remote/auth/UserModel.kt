package com.github.clockworkclyde.models.remote.auth

import com.github.clockworkclyde.models.local.auth.User
import com.github.clockworkclyde.models.mappers.IConvertableTo

data class UserModel(val uid: String, val name: String) : IConvertableTo<User> {

    override fun convertTo(): User {
        return User(uid = uid, name = name)
    }
}
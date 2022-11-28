package com.github.clockworkclyde.basedeliverymvvm.domain.auth

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    operator fun invoke(): String? = firebaseAuth.currentUser?.uid
}
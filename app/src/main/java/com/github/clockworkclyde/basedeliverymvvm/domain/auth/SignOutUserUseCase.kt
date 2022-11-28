package com.github.clockworkclyde.basedeliverymvvm.domain.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class SignOutUserUseCase @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    operator fun invoke(): Flow<Boolean> = flow {
        try {
            firebaseAuth.signOut()
            emit(true)
        } catch (e: Exception) {
            Timber.e(e)
            emit(false)
        }
    }
}
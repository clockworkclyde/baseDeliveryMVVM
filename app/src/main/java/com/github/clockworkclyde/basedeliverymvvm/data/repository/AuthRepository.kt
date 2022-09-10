package com.github.clockworkclyde.basedeliverymvvm.data.repository

import com.github.clockworkclyde.models.local.user.User
import com.github.clockworkclyde.models.remote.auth.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

sealed class Response<out T> {
    object Loading : Response<Nothing>()
    data class Success<T>(val data: T) : Response<T>()
    data class Error(val error: Throwable) : Response<Nothing>()
}

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    fun signOut(): Flow<Boolean> = flow {
        try {
            firebaseAuth.signOut()
            emit(true)
        } catch (e: Exception) {
            Timber.e(e)
            emit(false)
        }
    }

    suspend fun signInWithCredential(credential: PhoneAuthCredential): Flow<Response<User>> =
        flow {
            emit(Response.Loading)
            try {
                val user = firebaseAuth.signInWithCredential(credential).await().user
                if (user != null) {
                    emit(Response.Success(getUser(user).convertTo()))
                }
            } catch (e: Exception) {
                // todo FirebaseAuthInvalidCredentialsException for invalid credential
                emit(Response.Error(e))
            }
        }

    fun getFirebaseAuthState() = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser != null)
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose { firebaseAuth.removeAuthStateListener(authStateListener) }
    }

    private fun getUser(firebaseUser: FirebaseUser): UserModel {
        val uid = firebaseUser.uid
        val name = firebaseUser.displayName ?: "User@${firebaseUser.uid.hashCode()}"
        return UserModel(uid, name)
    }
}
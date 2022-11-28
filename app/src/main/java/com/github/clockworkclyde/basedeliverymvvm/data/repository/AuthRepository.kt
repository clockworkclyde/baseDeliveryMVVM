package com.github.clockworkclyde.basedeliverymvvm.data.repository

import com.github.clockworkclyde.models.local.auth.User
import com.github.clockworkclyde.models.remote.auth.UserModel
import com.github.clockworkclyde.models.remote.base.Response
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

// todo переделать в юзкейсы
class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    fun getUserUid(): String? = firebaseAuth.currentUser?.uid

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
            try {
                val user = firebaseAuth.signInWithCredential(credential).await().user
                if (user != null) {
                    emit(Response.Success(getUser(user).convertTo()))
                }
            } catch (e: Exception) {
                // todo FirebaseAuthInvalidCredentialsException for invalid credential
                Timber.e(e)
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
        val phone = firebaseUser.phoneNumber ?: ""
        return UserModel(uid, name, phone)
    }

    fun tryToGetCurrentUser(): Response<User> {
        return try {
            Response.Success(getUser(firebaseAuth.currentUser!!).convertTo())
        } catch (e: Exception) {
            Timber.e(e)
            Response.Error(e)
        }
    }
}
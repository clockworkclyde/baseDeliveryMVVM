package com.github.clockworkclyde.basedeliverymvvm.domain.auth

import com.github.clockworkclyde.models.local.auth.User
import com.github.clockworkclyde.models.remote.base.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class SignInWithCredentialUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userModelMapper: UserModelMapper
) {

    suspend operator fun invoke(credential: PhoneAuthCredential): Flow<Response<User>> =
        flow {
            try {
                val user = firebaseAuth.signInWithCredential(credential).await().user
                if (user != null) {
                    emit(Response.Success(userModelMapper(user).convertTo()))
                }
            } catch (e: Exception) {
                // todo FirebaseAuthInvalidCredentialsException for invalid credential
                Timber.e(e)
                emit(Response.Error(e))
            }
        }
}
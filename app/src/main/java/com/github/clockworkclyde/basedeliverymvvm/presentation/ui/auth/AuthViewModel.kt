package com.github.clockworkclyde.basedeliverymvvm.presentation.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.github.clockworkclyde.basedeliverymvvm.data.repository.AuthRepository
import com.github.clockworkclyde.basedeliverymvvm.data.repository.Response
import com.github.clockworkclyde.basedeliverymvvm.presentation.ui.base.BaseViewModel
import com.github.clockworkclyde.models.local.auth.User
import com.github.clockworkclyde.models.local.auth.UserPref
import com.google.firebase.auth.PhoneAuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _phoneNumber = MutableLiveData("")
    val phoneNumber: LiveData<String> get() = _phoneNumber

    private val _verificationId = MutableLiveData("")
    val verificationId: LiveData<String> get() = _verificationId

    private val _signInState = MutableLiveData<Response<User>>()
    val signInState: LiveData<Response<User>> get() = _signInState

    fun signInWithCredential(credential: PhoneAuthCredential) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            authRepository.signInWithCredential(credential)
                .collect { _signInState.postValue(it) }
        }
    }

    fun signOut() = liveData(Dispatchers.IO) {
        authRepository.signOut().collect { emit(it) }
    }

    /** emits /true/ if user is authenticated **/
    fun getAuthState() = liveData(Dispatchers.IO) {
        authRepository.getFirebaseAuthState().collect { emit(it) }
    }

    fun saveUser(user: User) {
        UserPref.uid = user.uid
        UserPref.username = user.name
    }

    fun setPhone(phone: String) {
        _phoneNumber.value = phone
    }

    fun setVerificationId(id: String) {
        _verificationId.value = id
    }

    fun clearVerificationData() {
        _verificationId.value = ""
        _phoneNumber.value = ""
    }

    fun countDownTimer(millisInFuture: Long, interval: Long) = liveData {
        var time = millisInFuture
        while (millisInFuture > 0) {
            time -= interval
            emit(time)
            delay(interval)
        }
    }
}
package com.github.clockworkclyde.basedeliverymvvm.domain.address

import com.github.clockworkclyde.basedeliverymvvm.data.repository.UserAddressRepository
import com.github.clockworkclyde.basedeliverymvvm.data.repository.AuthRepository
import javax.inject.Inject

class GetUserAddressesUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val addressRepository: UserAddressRepository
) {

    suspend operator fun invoke() =
        authRepository.getUserUid()?.let { uid ->
            addressRepository.getUserAddresses(uid)
        }
}
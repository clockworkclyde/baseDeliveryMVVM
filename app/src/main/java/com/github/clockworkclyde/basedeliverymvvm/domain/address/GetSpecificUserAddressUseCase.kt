package com.github.clockworkclyde.basedeliverymvvm.domain.address

import com.github.clockworkclyde.basedeliverymvvm.data.repository.UserAddressRepository
import com.github.clockworkclyde.basedeliverymvvm.data.repository.AuthRepository
import com.github.clockworkclyde.models.ui.address.AddressItem
import javax.inject.Inject

class GetSpecificUserAddressUseCase @Inject constructor(
    private val addressRepository: UserAddressRepository,
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(id: String): AddressItem? =
        authRepository.getUserUid()?.let { uid ->
            addressRepository.getUserAddressById(uid = uid, id = id)
        }
}
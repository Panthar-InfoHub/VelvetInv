package org.sharad.velvetinvestment.features.auth.domain.usecase

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.auth.domain.repository.MpinRepo

class VerifyMpinUseCase(
    private val repository: MpinRepo
) {
    suspend operator fun invoke(mpin: String): NetworkResponse<Unit, ErrorDomain> {
        return repository.verifyMpin(mpin)
    }
}

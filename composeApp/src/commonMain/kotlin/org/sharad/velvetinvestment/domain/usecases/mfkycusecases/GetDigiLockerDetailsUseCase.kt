package org.sharad.velvetinvestment.domain.usecases.mfkycusecases

import org.sharad.velvetinvestment.domain.models.mfkyc.DigiLockerDetailsDomain
import org.sharad.velvetinvestment.domain.repository.MFKYCRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetDigiLockerDetailsUseCase(
    private val repository: MFKYCRepository
) {
    suspend operator fun invoke(): NetworkResponse<DigiLockerDetailsDomain, ErrorDomain> {
        return repository.getDigiLockerDetails()
    }
}
package org.sharad.velvetinvestment.domain.usecases.mfkycusecases

import org.sharad.velvetinvestment.domain.models.mfkyc.KYCInitInfo
import org.sharad.velvetinvestment.domain.models.mfkyc.KycFormDataDomain
import org.sharad.velvetinvestment.domain.repository.MFKYCRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class SubmitKycFormUseCase(
    private val repository: MFKYCRepository

) {
    suspend operator fun invoke(data: KycFormDataDomain): NetworkResponse<Unit, ErrorDomain> {
        return repository.uploadKYCFormData(data)
    }
}
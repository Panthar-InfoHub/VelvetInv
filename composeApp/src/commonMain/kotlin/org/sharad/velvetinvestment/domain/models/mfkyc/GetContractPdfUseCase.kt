package org.sharad.velvetinvestment.domain.models.mfkyc

import org.sharad.velvetinvestment.domain.repository.MFKYCRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetContractPdfUseCase(
    private val repository: MFKYCRepository
) {
    suspend operator fun invoke(): NetworkResponse<String, ErrorDomain> {
        return repository.getContractPdf()
    }
}
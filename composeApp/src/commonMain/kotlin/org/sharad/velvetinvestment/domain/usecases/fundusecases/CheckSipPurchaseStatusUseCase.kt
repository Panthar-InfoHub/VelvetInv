package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.domain.SIPStatus
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class CheckSipPurchaseStatusUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(mandateId: String): NetworkResponse<SIPStatus, ErrorDomain> {
        return repository.checkSipPurchaseStatus(mandateId)
    }
}

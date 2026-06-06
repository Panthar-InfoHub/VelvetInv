package org.sharad.velvetinvestment.domain.usecases.fundusecases

import org.sharad.velvetinvestment.domain.models.usercart.SipItemDomain
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class PurchaseSipFundUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(mandateId: String, sipItems: List<SipItemDomain>): NetworkResponse<String, ErrorDomain> {
        return repository.purchaseSipFund(mandateId=mandateId, sipItems=sipItems)
    }
}
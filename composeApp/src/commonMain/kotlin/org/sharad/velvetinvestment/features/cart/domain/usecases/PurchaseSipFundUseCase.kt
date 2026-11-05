package org.sharad.velvetinvestment.features.cart.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.cart.domain.models.SipItemDomain
import org.sharad.velvetinvestment.features.cart.domain.repository.CartRepository

class PurchaseSipFundUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(mandateId: String, sipItems: List<SipItemDomain>): NetworkResponse<String, ErrorDomain> {
        return repository.purchaseSip(mandateId=mandateId, sipItems=sipItems)
    }
}
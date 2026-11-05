package org.sharad.velvetinvestment.features.cart.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.cart.domain.models.MutualFundPurchaseInitiateDomain
import org.sharad.velvetinvestment.features.cart.domain.models.SipItemDomain
import org.sharad.velvetinvestment.features.cart.domain.repository.CartRepository

class InitiateSipPurchaseUseCase(
    private val repository: CartRepository
) {
    suspend operator fun invoke(sipData: List<SipItemDomain>): NetworkResponse<MutualFundPurchaseInitiateDomain, ErrorDomain> {
        return repository.initiateSipPurchase(sipData=sipData)
    }
}

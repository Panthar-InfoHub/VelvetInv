package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.domain.models.tradingaccount.prefilled.TradingAccountPrefilledDomain
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetTradingAccountPrefilledDataUseCase(
    private val repository: UserAuth
) {
    suspend operator fun invoke(): NetworkResponse<TradingAccountPrefilledDomain, ErrorDomain> {
        return repository.getTradingAccountPrefilledData()
    }
}
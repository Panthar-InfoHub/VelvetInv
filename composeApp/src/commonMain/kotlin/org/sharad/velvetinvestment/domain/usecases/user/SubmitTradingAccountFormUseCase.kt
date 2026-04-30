package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.domain.models.tradingaccount.TradingAccountFormDomain
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class SubmitTradingAccountFormUseCase(
    private val repository: UserAuth
) {
    suspend operator fun invoke(data: TradingAccountFormDomain): NetworkResponse<String, ErrorDomain> {
        return repository.submitTradingAccountForm(data)
    }
}

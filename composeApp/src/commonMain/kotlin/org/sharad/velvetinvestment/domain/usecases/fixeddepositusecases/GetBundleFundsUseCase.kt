package org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases

import org.sharad.velvetinvestment.domain.models.mutualfunds.BundledMutualFundDomain
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetBundleFundsUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke(
        bundleKey: String
    ): NetworkResponse<BundledMutualFundDomain, ErrorDomain> {
        return repository.getBundleFunds(bundleKey)
    }
}
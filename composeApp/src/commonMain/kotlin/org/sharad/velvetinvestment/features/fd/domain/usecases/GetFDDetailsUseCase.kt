package org.sharad.velvetinvestment.features.fd.domain.usecases

import org.sharad.velvetinvestment.features.fd.domain.model.CustomerType
import org.sharad.velvetinvestment.features.fd.domain.model.FDDetailsDomain
import org.sharad.velvetinvestment.features.fd.domain.model.PayoutType
import org.sharad.velvetinvestment.features.fd.domain.repository.FixedDepositRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetFDDetailsUseCase(
    private val repository: FixedDepositRepository
) {
    suspend operator fun invoke(
        id: String,
        payoutFrequency: PayoutType? = null,
        customerType: CustomerType? = null
    ): NetworkResponse<FDDetailsDomain, ErrorDomain> {
        return repository.getFDDetails(id, payoutFrequency, customerType)
    }
}
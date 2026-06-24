package org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases

import org.sharad.velvetinvestment.domain.models.fd.FDDetailsDomain
import org.sharad.velvetinvestment.domain.repository.FixedDepositRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetFDDetailsUseCase(
    private val repository: FixedDepositRepository
) {
    suspend operator fun invoke(
        id: String,
        customerType: String? = null
    ): NetworkResponse<FDDetailsDomain, ErrorDomain> {
        return repository.getFDDetails(id, customerType)
    }
}
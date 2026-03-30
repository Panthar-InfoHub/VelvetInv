package org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases

import org.sharad.velvetinvestment.data.remote.model.purchasefd.PurchaseFDBodyDto
import org.sharad.velvetinvestment.domain.models.fd.FDDetailsDomain
import org.sharad.velvetinvestment.domain.repository.FixedDepositRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class PurchaseFDUseCase(
    private val repository: FixedDepositRepository
) {
    suspend operator fun invoke(
       data: PurchaseFDBodyDto
    ): NetworkResponse<String, ErrorDomain> {
        return repository.purchaseFD(data)
    }
}
package org.sharad.velvetinvestment.features.fd.domain.usecases

import org.sharad.velvetinvestment.features.fd.data.models.dto.PurchaseFDBodyDto
import org.sharad.velvetinvestment.features.fd.domain.repository.FixedDepositRepository
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

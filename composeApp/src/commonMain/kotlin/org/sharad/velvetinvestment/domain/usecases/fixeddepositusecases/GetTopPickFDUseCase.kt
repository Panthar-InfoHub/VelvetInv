package org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases

import org.sharad.velvetinvestment.domain.models.fixeddeposits.CategoryFixedDepositDomain
import org.sharad.velvetinvestment.domain.repository.FixedDepositRepository
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetTopPickFDUseCase(
    private val repository: FixedDepositRepository
) {

    suspend operator fun invoke():
            NetworkResponse<List<CategoryFixedDepositDomain>, NetworkError> {

        return repository.getTopPickFDs()
    }
}
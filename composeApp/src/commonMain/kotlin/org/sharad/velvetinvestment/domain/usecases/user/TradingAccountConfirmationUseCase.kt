package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class TradingAccountConfirmationUseCase(
    private val repository: UserAuth
) {
    suspend operator fun invoke(
        taxStatus: String,
        holdingNature: String,
        jointHolderName1: String,
        jointHolderName2: String,
        guardianName: String,
        isMinor: Boolean
    ): NetworkResponse<Unit, ErrorDomain> {
        return repository.tradingAccountConfirmation(
            taxStatus = taxStatus,
            holdingNature = holdingNature,
            jointHolderName1 = jointHolderName1,
            jointHolderName2 = jointHolderName2,
            guardianName = guardianName,
            isMinor = isMinor
        )
    }
}

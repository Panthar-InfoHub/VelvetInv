package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.domain.models.fire.FireCombinedDomainModel
import org.sharad.velvetinvestment.domain.models.fire.FireReportDomainModel
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface UserFinance {

    suspend fun getFireReport():
            NetworkResponse<FireCombinedDomainModel, NetworkError>
}
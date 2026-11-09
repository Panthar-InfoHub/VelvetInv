package org.sharad.velvetinvestment.features.insurance.domain.repository

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface InsuranceRepository {
    /** Asks the team to get in touch about insurance; [type] is "CALL" or "CHAT". */
    suspend fun requestConnection(type: String, message: String): NetworkResponse<Unit, ErrorDomain>
}

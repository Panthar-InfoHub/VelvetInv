package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.domain.models.fire.FireReportDomain
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface UserFinance {

    suspend fun getFireReport():
            NetworkResponse<FireReportDomain, ErrorDomain>

    suspend fun getFirePdf():
            NetworkResponse<ByteArray, ErrorDomain>
}
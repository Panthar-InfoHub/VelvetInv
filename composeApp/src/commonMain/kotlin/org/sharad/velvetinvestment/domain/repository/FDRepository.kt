package org.sharad.velvetinvestment.domain.repository

import com.sharad.surakshakawachneo.utils.Networking.NetworkError
import com.sharad.surakshakawachneo.utils.Networking.NetworkResponse
import org.sharad.velvetinvestment.domain.models.portfolio.FDDetailsDomain
import org.sharad.velvetinvestment.presentation.portfolio.models.FDCardData

interface FDRepository {

    suspend fun getFDs():
            NetworkResponse<List<FDCardData>, NetworkError>
    suspend fun getFDDetails(fdId: String): NetworkResponse<FDDetailsDomain, NetworkError>
}

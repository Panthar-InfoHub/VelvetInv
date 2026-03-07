package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.domain.models.fixeddeposits.CategoryFixedDepositDomain
import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositDomain
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface FixedDepositRepository {

    suspend fun getTopPickFDs():
            NetworkResponse<List<CategoryFixedDepositDomain>, NetworkError>

    suspend fun getFDSearchResult(searchId: String):
            NetworkResponse<List<FixedDepositDomain>, NetworkError>
}
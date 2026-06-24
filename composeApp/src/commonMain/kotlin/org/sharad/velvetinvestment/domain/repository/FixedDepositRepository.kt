package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.data.remote.model.purchasefd.PurchaseFDBodyDto
import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.fd.FDDetailsDomain
import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositDomain
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface FixedDepositRepository {

    suspend fun getFDSearchResult(
        maxDeposit: Double?,
        minDeposit: Double?,
        payoutFrequency: String?,
        tenure: String?,
        limit: Int?,
        page: Int?,
        search: String?,
    ): NetworkResponse<PaginatedData<FixedDepositDomain>, ErrorDomain>

    suspend fun getFDDetails(id: String, customerType:String?): NetworkResponse<FDDetailsDomain, ErrorDomain>

    suspend fun purchaseFD(data: PurchaseFDBodyDto): NetworkResponse<String, ErrorDomain>
}
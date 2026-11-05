package org.sharad.velvetinvestment.features.fd.domain.repository

import org.sharad.velvetinvestment.features.fd.data.models.dto.PurchaseFDBodyDto
import org.sharad.velvetinvestment.features.fd.domain.model.CustomerType
import org.sharad.velvetinvestment.features.fd.domain.model.FDDetailsDomain
import org.sharad.velvetinvestment.features.fd.domain.model.FixedDepositDomain
import org.sharad.velvetinvestment.features.fd.domain.model.PaginatedData
import org.sharad.velvetinvestment.features.fd.domain.model.PayoutType
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface FixedDepositRepository {

    suspend fun getFDSearchResult(
        maxDeposit: Double? = null,
        minDeposit: Double? = null,
        payoutFrequency: String? = null,
        tenure: String? = null,
        limit: Int? = null,
        page: Int? = null,
        search: String? = null,
    ): NetworkResponse<PaginatedData<FixedDepositDomain>, ErrorDomain>

    /** Without a [payoutFrequency] or [customerType] the server returns the rates for all of them. */
    suspend fun getFDDetails(
        id: String,
        payoutFrequency: PayoutType? = null,
        customerType: CustomerType? = null
    ): NetworkResponse<FDDetailsDomain, ErrorDomain>

    suspend fun purchaseFD(data: PurchaseFDBodyDto): NetworkResponse<String, ErrorDomain>
}

package org.sharad.velvetinvestment.features.fd.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getFDUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest
import org.sharad.velvetinvestment.features.fd.data.mapper.toDomain
import org.sharad.velvetinvestment.features.fd.data.models.dto.FDDetailsDto
import org.sharad.velvetinvestment.features.fd.data.models.dto.FixedDepositListDto
import org.sharad.velvetinvestment.features.fd.data.models.dto.PurchaseFDBodyDto
import org.sharad.velvetinvestment.features.fd.data.models.dto.PurchaseFDDto
import org.sharad.velvetinvestment.features.fd.domain.model.CustomerType
import org.sharad.velvetinvestment.features.fd.domain.model.FDDetailsDomain
import org.sharad.velvetinvestment.features.fd.domain.model.FixedDepositDomain
import org.sharad.velvetinvestment.features.fd.domain.model.PaginatedData
import org.sharad.velvetinvestment.features.fd.domain.model.PayoutType
import org.sharad.velvetinvestment.features.fd.domain.repository.FixedDepositRepository

class FixedDepositRepo(
    private val client: HttpClient
): FixedDepositRepository {

    override suspend fun getFDSearchResult(
        maxDeposit: Double?,
        minDeposit: Double?,
        payoutFrequency: String?,
        tenure: String?,
        limit: Int?,
        page: Int?,
        search: String?
    ): NetworkResponse<PaginatedData<FixedDepositDomain>, ErrorDomain> {
        val response= safeRequest<FixedDepositListDto> {
            client.get(
                getFDUrl("/fd")
            ) {
                parameter("max_deposit", maxDeposit)
                parameter("min_deposit", minDeposit)
                parameter("payout_frequency", payoutFrequency)
                parameter("tenure", tenure)
                parameter("limit", limit)
                parameter("page", page)
                parameter("search", search)
            }
        }
        when(response){
            is NetworkResponse.Error -> {
                return response
            }
            is NetworkResponse.Success -> {
                val data=response.data
                return NetworkResponse.Success(data.toDomain())
            }
        }
    }

    override suspend fun getFDDetails(
        id: String,
        payoutFrequency: PayoutType?,
        customerType: CustomerType?
    ): NetworkResponse<FDDetailsDomain, ErrorDomain> {
        val response= safeRequest<FDDetailsDto> {
            client.get(
                getFDUrl("/fd/$id")
            ) {
                // A null value adds no parameter, so callers that pass neither get every rate.
                parameter("payout_frequency", payoutFrequency?.id)
                parameter("customer_type", customerType?.name)
            }
        }
        when (response) {
            is NetworkResponse.Error -> {
                return response
            }
            is NetworkResponse.Success -> {
                val data = response.data
                return NetworkResponse.Success(data.toDomain())
            }
        }
    }

    override suspend fun purchaseFD(data: PurchaseFDBodyDto): NetworkResponse<String, ErrorDomain> {
        val response= safeRequest<PurchaseFDDto> {
            client.post (
                getFDUrl("/fd/purchase-url")
            ){
                setBody(data)
            }
        }
        when (response) {
            is NetworkResponse.Error -> {
                return response
            }
            is NetworkResponse.Success -> {
                val data = response.data
                return NetworkResponse.Success(data.data)
            }
        }
    }
}
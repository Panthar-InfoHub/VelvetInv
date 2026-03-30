package org.sharad.velvetinvestment.data.remote.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.data.remote.mapper.toDomain
import org.sharad.velvetinvestment.data.remote.model.fddetails.FDDetailsDto
import org.sharad.velvetinvestment.data.remote.model.getfds.FixedDepositListDto
import org.sharad.velvetinvestment.data.remote.model.purchasefd.PurchaseFDBodyDto
import org.sharad.velvetinvestment.data.remote.model.purchasefd.PurchaseFDDto
import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.fd.FDDetailsDomain
import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositDomain
import org.sharad.velvetinvestment.domain.repository.FixedDepositRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest

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
                getUrl("/fd")
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

    override suspend fun getFDDetails(id: String): NetworkResponse<FDDetailsDomain, ErrorDomain> {
        val response= safeRequest<FDDetailsDto> {
            client.get(
                getUrl("/fd/$id")
            )
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
                getUrl("/fd/purchase-url")
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
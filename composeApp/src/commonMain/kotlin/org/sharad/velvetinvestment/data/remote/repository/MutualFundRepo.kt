package org.sharad.velvetinvestment.data.remote.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import org.sharad.velvetinvestment.data.remote.mapper.toDomain
import org.sharad.velvetinvestment.data.remote.mapper.toPaginatedDomain
import org.sharad.velvetinvestment.data.remote.model.getmf.MutualFundDto
import org.sharad.velvetinvestment.data.remote.model.mfdetails.MutualFundsDetailDto
import org.sharad.velvetinvestment.data.remote.model.mfgraph.MFGraphDto
import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.explore.MutualFundTopPicksDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.CategoryMutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDetailsDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphDomain
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.presentation.portfolio.models.FundListCardData
import org.sharad.velvetinvestment.presentation.portfolio.models.MutualFundDashBoardData
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest

class MutualFundRepo(
    private val client: HttpClient
): MutualFundRepository {
    override suspend fun getPortfolioMutualFunds(): NetworkResponse<List<FundListCardData>, NetworkError> {
        return NetworkResponse.Success(emptyList())
    }

    override suspend fun getDashboard(): NetworkResponse<MutualFundDashBoardData, NetworkError> {
        return NetworkResponse.Error(NetworkError.UNKNOWN)
    }

    override suspend fun getMutualFundTopPicks(): NetworkResponse<List<MutualFundTopPicksDomain>, NetworkError> {
        return NetworkResponse.Error(NetworkError.UNKNOWN)
    }

    override suspend fun getCategoryMutualFunds(): NetworkResponse<List<CategoryMutualFundDomain>, NetworkError> {
        return NetworkResponse.Error(NetworkError.UNKNOWN)
    }

    override suspend fun getMutualFundsBySearch(
        search: String?,
        page: Int?,
        limit: Int?,
        sort: String?,
        risk: Int?,
        category: String?,
    ):NetworkResponse<PaginatedData<MutualFundDomain>, ErrorDomain> {
        val response = safeRequest<MutualFundDto> {
            client.get(getUrl("/mf")) {
                search?.let { parameter("search", it) }
                page?.let { parameter("page", it) }
                limit?.let { parameter("limit", it) }
                sort?.let { parameter("sort", it) }
                risk?.let { parameter("risk", it) }
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toPaginatedDomain())
            }

            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }

    }

    override suspend fun getMutualFundDetails(id: String): NetworkResponse<MutualFundDetailsDomain, ErrorDomain> {
        val response = safeRequest< MutualFundsDetailDto> {
            client.get(getUrl("/mf/$id")) {
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toDomain())
            }

            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }

    override suspend fun getMutualFundGraph(
        id: String,
        period: String,
    ): NetworkResponse<MutualFundGraphDomain, ErrorDomain> {
        val response = safeRequest<MFGraphDto> {
            client.get(getUrl("/mf/history/${id}")) {
                parameter("period", period)
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toDomain())
            }

            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }
}
package org.sharad.velvetinvestment.data.remote.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.data.remote.mapper.toDomain
import org.sharad.velvetinvestment.data.remote.mapper.toPaginatedDomain
import org.sharad.velvetinvestment.data.remote.model.bundlecart.AddBundleLumpsumRequest
import org.sharad.velvetinvestment.data.remote.model.bundlecart.AddBundleSipRequest
import org.sharad.velvetinvestment.data.remote.model.bundledfundbyid.BundledFundByIdDto
import org.sharad.velvetinvestment.data.remote.model.bundledfunds.BundledFundsDto
import org.sharad.velvetinvestment.data.remote.model.cartaddlumpsum.AddCartLumpSumRequest
import org.sharad.velvetinvestment.data.remote.model.cartaddlumpsum.AddCartLumpSumResponseDto
import org.sharad.velvetinvestment.data.remote.model.cartaddsip.AddCartSipRequest
import org.sharad.velvetinvestment.data.remote.model.cartaddsip.AddCartSipResponseDto
import org.sharad.velvetinvestment.data.remote.model.cartpurchase.CartPurchaseDto
import org.sharad.velvetinvestment.data.remote.model.getmf.MutualFundDto
import org.sharad.velvetinvestment.data.remote.model.mfdetails.MutualFundsDetailDto
import org.sharad.velvetinvestment.data.remote.model.mfgraph.MFGraphDto
import org.sharad.velvetinvestment.data.remote.model.mutualfundcombined.CombinedFundsDto
import org.sharad.velvetinvestment.data.remote.model.usercart.UserCartDto
import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.explore.MutualFundTopPicksDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundledMutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.CombinedFundsDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDetailsDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphDomain
import org.sharad.velvetinvestment.domain.models.usercart.UserCartDomain
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.presentation.portfolio.models.FundListCardData
import org.sharad.velvetinvestment.presentation.portfolio.models.MutualFundDashBoardData
import org.sharad.velvetinvestment.utils.CartInfo
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.ErrorType
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest
import org.sharad.velvetinvestment.utils.networking.safeUnitRequest

class MutualFundRepo(
    private val client: HttpClient
): MutualFundRepository {
    override suspend fun getPortfolioMutualFunds(): NetworkResponse<List<FundListCardData>, ErrorDomain> {
        return NetworkResponse.Success(emptyList())
    }

    override suspend fun getDashboard(): NetworkResponse<MutualFundDashBoardData, ErrorDomain> {
        return NetworkResponse.Error(ErrorDomain(0, "Unknown", ErrorType.UNKNOWN))
    }

    override suspend fun getMutualFundTopPicks(): NetworkResponse<List<MutualFundTopPicksDomain>, ErrorDomain> {
        return NetworkResponse.Error(ErrorDomain(0, "Unknown", ErrorType.UNKNOWN))
    }

    override suspend fun getCategoryMutualFunds(
        page: Int?,
        limit: Int?
    ): NetworkResponse<List<BundledMutualFundDomain>, ErrorDomain> {

        val response = safeRequest<BundledFundsDto> {
            client.get(getUrl("/bundles")) {
                page?.let { parameter("page", it) }
                limit?.let { parameter("limit", it) }
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

    override suspend fun getMutualFundCart(): NetworkResponse<UserCartDomain, ErrorDomain> {
        val response= safeRequest<UserCartDto> {
            client.get(getUrl("/user/cart"))
        }

        return when(response){
            is NetworkResponse.Error-> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                val domain= response.data.toDomain()
                CartInfo.updateFundAmount(domain.sipItems.size+ domain.lumpSumItems.size)
                NetworkResponse.Success(domain)
            }
        }
    }

    override suspend fun deleteCartItem(id: String): NetworkResponse<Unit, ErrorDomain> {
        val response= safeUnitRequest {
            client.delete(getUrl("/mf/remove-cart-item")) {
                parameter("cart_item_id", id)
            }
        }
        return when(response){
            is NetworkResponse.Error-> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }

    override suspend fun addToCartLumSumFund(
        id: String,
        amount: Long,
    ): NetworkResponse<Unit, ErrorDomain> {
        val response= safeRequest<AddCartLumpSumResponseDto> {
            client.post(getUrl("/mf/lumpsum-cart")){
                setBody(
                    AddCartLumpSumRequest(
                        amount = amount,
                        mf_product_id = id
                    )
                )
            }
        }

        return when(response){
            is NetworkResponse.Error-> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }

    override suspend fun addToCartSipFund(request: AddCartSipRequest): NetworkResponse<Unit, ErrorDomain> {
        val response= safeRequest<AddCartSipResponseDto> {
            client.post(getUrl("/mf/sip-cart")){
                setBody(request)
            }
        }

        return when(response){
            is NetworkResponse.Error-> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }

    override suspend fun purchaseLumSumFund(): NetworkResponse<String, ErrorDomain> {
        val response=safeRequest<CartPurchaseDto> {
            client.post(getUrl("/mf/purchase-lumpsum"))
        }
        return when(response){
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.data)
            }
        }
    }

    override suspend fun purchaseSipFund(): NetworkResponse<String, ErrorDomain> {
        val response=safeRequest<CartPurchaseDto> {
            client.post(getUrl("/mf/purchase-sip"))
        }
        return when(response){
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.data)
            }
        }
    }

    override suspend fun getCombinedCategoryMutualFunds(): NetworkResponse<CombinedFundsDomain, ErrorDomain> {
        val response = safeRequest<CombinedFundsDto> {
            client.get(getUrl("/frontend/mf-data"))
        }

        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toDomain())
            }
        }
    }

    override suspend fun getBundleFunds(bundleKey: String): NetworkResponse<BundledMutualFundDomain, ErrorDomain> {
        val response = safeRequest<BundledFundByIdDto> {
            client.get(getUrl("/bundles/$bundleKey"))
        }

        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toDomain())
            }
        }
    }

    override suspend fun addBundleToCartLumpsum(
        bundleId: String,
        amount: Long
    ): NetworkResponse<Unit, ErrorDomain> {
        val response = safeUnitRequest {
            client.post(getUrl("/mf/bundle-cart")) {
                setBody(
                    AddBundleLumpsumRequest(
                        bundle_id = bundleId,
                        amount = amount
                    )
                )
            }
        }
        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }

    override suspend fun addBundleToCartSip(
        request: AddBundleSipRequest
    ): NetworkResponse<Unit, ErrorDomain> {
        val response = safeUnitRequest {
            client.post(getUrl("/mf/bundle-cart")) {
                setBody(request)
            }
        }
        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }
}
